package com.qg24.dao;

import com.qg24.po.entity.BannedIp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannedIpMapper {
    //添加被封禁的ip
    int addIp(BannedIp bannedIp);


    //获取所有被封禁ip地址
    List<String> selectAllIps();
}
