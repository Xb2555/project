package com.qg24.Websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qg24.dao.ProjectMapper;
import com.qg24.dao.UserMapper;
import com.qg24.dao.WarningMessageMapper;
import com.qg24.interceptor.CustomConfigurator;
import com.qg24.po.dto.WebsocketDTO;
import com.qg24.po.entity.Project;
import com.qg24.po.entity.ProjectPresentationData;
import com.qg24.po.entity.User;
import com.qg24.po.entity.WarningMessage;
import com.qg24.po.result.Result;
import com.qg24.po.result.WebsocketMessage;
import com.qg24.po.vo.AdminQueryUserVO;
import com.qg24.utils.JwtUtils;
import com.qg24.utils.SpringContextUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint(value = "/websocket/{userId}"
        , configurator = CustomConfigurator.class
)
public class WebSocket {
    /**
     * 用于存放在线连接数
     */
    private static AtomicInteger onlineSessionCount = new AtomicInteger(0);
    /**
     * 存放所有在线的客户端
     */
    private static Map<String,Session> onlineSessionPool = new ConcurrentHashMap<>();

    private UserMapper userMapper;
    /**
     * 用于心跳机制判断用户是否占用线路
     */
    private final ConcurrentHashMap<Session,Long> currentSessionMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    /**
     * 连接sid和连接会话
     */
    private String userId;
    private Session session;

    /**
     * 判断用户是否占线
     */
    public WebSocket() {
        scheduler.scheduleAtFixedRate(this::checkSessions, 1, 1, TimeUnit.MINUTES);
    }
    /**
     * session 与某客户端的连接会话，用于给客户端发送消息
     */
    @OnOpen
    public void onopen(Session session,@PathParam(value = "userId") String userId) throws IOException {
        Map<String, List<String>> params = session.getRequestParameterMap();
        List<String> tokens = params.get("token");
        String token = null;
        if (tokens != null){
            token = tokens.get(0);
        }
        if(onlineSessionPool.containsKey(userId)){
            //该用户已上线
            sendToOne(userId,JSON.toJSONString(new WebsocketMessage(null,null,"multiLog", null)));
            Session sessionAdmin = onlineSessionPool.get(userId);
            //关闭连接
            onclose(userId,sessionAdmin);
        }else{
            if(!userId.equals("admin")){
                //不是管理员登录,判断是否是本人
                this.userId=userId;
                this.session=session;
                onlineSessionPool.put(userId,session);//加入在线人数池
                currentSessionMap.put(session,System.currentTimeMillis());//放入心跳判断池
                onlineSessionCount.incrementAndGet();//在线人数加1
                if(checkAuthor(session,userId,token)){
                    sendToOne(userId,JSON.toJSONString(Result.success("连接成功")));//发送成功消息给客户端
                    System.out.println(userId+"连接成功");
                    if(onlineSessionPool.containsKey("admin")){
                        //如果管理员在线，则发送在线用户数据
                        sendToOne("admin",JSON.toJSONString(new WebsocketMessage(null,null,null, getReturnList())));
                    }
                }else{
                    //不存在该用户
                    sendToOne(userId,JSON.toJSONString(new WebsocketMessage(null,null,"errorLog",null)));
                    onclose(userId,session);
                }
            }else{
                //为管理员
                this.userId=userId;
                this.session=session;
                onlineSessionPool.put(userId,session);//加入在线人数池
                currentSessionMap.put(session,System.currentTimeMillis());//放入心跳判断池
                onlineSessionCount.incrementAndGet();//在线人数加1
                //判断是否是管理员
                if(checkAuthor(session,userId,token)){
                    //是管理员
                    sendToOne(userId,JSON.toJSONString(Result.success("连接成功")));//发送成功消息给客户端
                    System.out.println(userId+"连接成功");
                    sendToOne("admin",JSON.toJSONString(new WebsocketMessage(null,null,null, getReturnList())));
                }else{
                    //不是管理员
                    sendToOne(userId,JSON.toJSONString(new WebsocketMessage(null,null,"noAccess", null)));
                    onclose(userId,session);
                }
            }
        }
    }


    @OnClose
    public void onclose(@PathParam(value = "userId") String username,Session session) throws IOException {
        if(onlineSessionPool.containsKey(username)){
            onlineSessionPool.remove(username);//从在线人数中移除
            onlineSessionCount.decrementAndGet();//在线人数减1
            session.close();
            if(onlineSessionPool.containsKey("admin")){
                //如果管理员在线，则发送在线用户数据
                sendToOne("admin",JSON.toJSONString(new WebsocketMessage(null,null,null, getReturnList())));
            }
        }
    }

    /**
     * 收到客户端消息的方法，由前端触发
     */
    @OnMessage
    public void onmessage(String message , Session session) throws IOException, InterruptedException {
        //更新心跳时间
        currentSessionMap.put(session,System.currentTimeMillis());
        System.out.println(message);
        //判断客户端调用什么函数
        JSONObject json = JSON.parseObject(message);
        WebsocketDTO dto = JSON.parseObject(message, WebsocketDTO.class);
        //判断是否为管理员操作
        if(dto.getMethodName().equals("logoutUser") || dto.getMethodName().equals("getUserList")){
            if(dto.getMethodName().equals("getUserList")){
                //管理员查看在线用户
                //验证是否为管理员
                if(onlineSessionPool.get("admin")==session){
                    //是管理员,发送在线用户列表
                    sendToOne("admin",JSON.toJSONString(new WebsocketMessage(null,null,null, getReturnList())));
                }else{
                    session.getAsyncRemote().sendText(JSON.toJSONString(new WebsocketMessage(null,null,"noAccess", null)));
                }
            }else {
                //管理员强制下线用户
                //验证是否为管理员
                if(onlineSessionPool.get("admin")==session){
                    //是管理员，下线用户
                    //发送下线信息给前端
                    sendToOne(dto.getData(), JSON.toJSONString(new WebsocketMessage(null,null,"offline", null)));
                    //调用onclose，给管理员发送新的在线用户列表
                    onclose(dto.getData(), onlineSessionPool.get(dto.getData()));
                }else{
                    session.getAsyncRemote().sendText(JSON.toJSONString(new WebsocketMessage(null,null,"noAccess", null)));
                }
            }
        }else if(dto.getMethodName().equals("checkMessage")) {
            //用户点击了确认消息
            WarningMessageMapper warningMessageMapper = SpringContextUtil.getBean(WarningMessageMapper.class);
            //更新查看标志
            warningMessageMapper.checkMessage(Integer.parseInt(dto.getData()),1);
        }else{
            //判断用户是否冻结
            if(onlineSessionPool.get("admin")!=session && checkUserFrozen(session)){
                //被冻结,下线
                sendToOne(getKey(onlineSessionPool,session),JSON.toJSONString(new WebsocketMessage(null,null,"frozen",null)));
                onclose(getKey(onlineSessionPool,session),session);
            }else{
                //未冻结，判断是否有报警信息
                checkWarningRate(session);
            }
        }
    }

    /**
     * 判断是否为管理员
     * @param session
     * @param userId
     * @return
     */
    private boolean checkAuthor(Session session,String userId,String token){
        UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
        if(token==null){
            // 获取 token
            token = (String) session.getUserProperties().get("token");
            if(token==null){
                token = (String) session.getUserProperties().get("protocol");
            }
            //获取token
            if (token != null && token.startsWith("Bearer ")) {
                // 去掉 "Bearer " 前缀
                token = token.substring(7);
            }else if(token==null){
                //token为空
                return false;
            }
        }
        Claims claims = JwtUtils.parseJwt(token);
        //获取用户权限
        String userRole = (String) claims.get("userRole");
        if(userId.equals("admin")){
            if(userRole.toLowerCase().equals(userId)){
                //是管理员
                return true;
            }else{
                //不是管理员
                return false;
            }
        }else {
            int id = 0;
            try {
                id = Integer.parseInt(userId);
            } catch (ArithmeticException e) {
                //不能转为int
                return false;
            }
            User user = userMapper.selectUserByUserId(id);
            if(user == null){
                //不存在该用户
                return false;
            }else{
                return true;
            }
        }
    }

    /**
     * 判断用户是否被冻结
     * @param session
     * @return
     */
    private boolean checkUserFrozen(Session session){
        UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
        User user = userMapper.selectUserByUserId(Integer.parseInt(getKey(onlineSessionPool,session)));
        //用户被冻结,等于0返回true，等于1返回false
        return user.getEnabled() == 0;
    }


    /**
     * 判断用户拥有的项目是否达到报警阈值
     * @param session
     */
    private void checkWarningRate(Session session){
        if(!getKey(onlineSessionPool, session).equals("admin")){
            //判断是否超过报警阈值
            try {
                // 创建一个自定义线程池
                ThreadPoolExecutor executor = new ThreadPoolExecutor(
                        5, // 核心线程数
                        5, // 最大线程数
                        1L, // 线程空闲时间
                        TimeUnit.SECONDS, // 时间单位
                        new LinkedBlockingQueue<>(100) // 任务队列
                );

                executor.submit(() -> {
                    try {
                        // 心跳机制,判断用户是否为发布者并判断是否超过报警阈值
                        ProjectMapper projectMapper = SpringContextUtil.getBean(ProjectMapper.class);
                        UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
                        WarningMessageMapper warningMessageMapper = SpringContextUtil.getBean(WarningMessageMapper.class);
                        // 拿到用户数据
                        User user = userMapper.selectUserByUserId(Integer.parseInt(getKey(onlineSessionPool, session)));
                        // 拿到用户拥有的项目列表
                        List<Project> projectList = projectMapper.selectProjectByUserId(user.getUsername());
                        if (projectList != null) {
                            for (Project project : projectList) {
                                WarningMessage wm = warningMessageMapper.selectNewMessageByProjectId(project.getProjectId());
                                //判断是否插入过信息表或还未点接收消息
                                if(wm == null || wm.getFlag()==0){
                                    // 判断是否超过阈值
                                    ProjectPresentationData data = projectMapper.getNewProjectData(project.getProjectId());
                                    if(data!=null){
                                        if (data.getErrorRate() > data.getWarningRate()) {
                                            // 超过阈值,提醒用户,并将报警信息插入表中
                                            if(wm == null){
                                                //如果没有消息
                                                WarningMessage warningMessage = new WarningMessage();
                                                warningMessage.setProjectId(data.getProjectId());
                                                warningMessage.setFlag(0);
                                                warningMessageMapper.insertMessage(warningMessage);
                                            }
                                            sendToOne(getKey(onlineSessionPool, session), JSON.toJSONString(new WebsocketMessage(project.getProjectId(),project.getProjectName(),"warning",null)));
                                            //隔6秒发
                                            Thread.sleep(6000);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        executor.shutdown();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用方法
     */
    @OnError
    public void onerror(Session session , Throwable throwable){
        System.out.println(throwable.getMessage());
    }

    /**
     * 群发消息
     */
    private void sendToAll(String msg){
        //遍历map集合
        onlineSessionPool.forEach((onlineSid,toSession)->{
            //排除自己
            if(!userId.equalsIgnoreCase(onlineSid)){
                toSession.getAsyncRemote().sendText(msg);
            }
        });
    }

    /**
     * 指定发送消息
     */
    private void sendToOne(String username,String msg){
        Session session1 = onlineSessionPool.get(username);
        if(session1==null){
            System.out.println("error");
        }else{
            session1.getAsyncRemote().sendText(msg);
        }
    }

    //通过值返回键
    public <String,Session> String getKey(Map<String,Session> map ,Session session){
        for(Map.Entry<String,Session> entry : map.entrySet()){
            if(entry.getValue().equals(session)){
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 用于判断用户是否占线
     */
    private void checkSessions() {
        long currentTime = System.currentTimeMillis();
        currentSessionMap.forEach((session, lastActivityTime) -> {
            if (currentTime - lastActivityTime > 2 * 60 * 1000) { // 3分钟超时
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentSessionMap.remove(session);
                //下线用户
                sendToOne(getKey(onlineSessionPool,session), JSON.toJSONString(new WebsocketMessage(null,null,"offline", null)));
                try {
                    onclose(getKey(onlineSessionPool,session),session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private List<AdminQueryUserVO> getReturnList(){
        List<AdminQueryUserVO> userList = new ArrayList<>();
        //查询用户数据
        userMapper=SpringContextUtil.getBean(UserMapper.class);
        List<User> users = userMapper.selectAllUsers();
        for(User user : users){
            //封装用户数据
            AdminQueryUserVO adminQueryUserVO = new AdminQueryUserVO();
            BeanUtils.copyProperties(user,adminQueryUserVO);
            //判断是否在线
            if(onlineSessionPool.containsKey(String.valueOf(user.getUserId()))){
                //在线
                adminQueryUserVO.setIsOnline("online");
            }else{
                adminQueryUserVO.setIsOnline("offline");
            }
            if(user.getEnabled()==1){
                adminQueryUserVO.setEnabled("未冻结");
            }else{
                adminQueryUserVO.setEnabled("冻结");
            }
            userList.add(adminQueryUserVO);
        }
        //将online排在前面
        userList.sort(Comparator.comparingInt(u -> u.getIsOnline().length()));
        return userList;
    }
}
