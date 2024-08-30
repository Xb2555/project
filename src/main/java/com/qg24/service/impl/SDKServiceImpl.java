package com.qg24.service.impl;

import com.qg24.dao.ProjectMapper;
import com.qg24.dao.SDKMapper;
import com.qg24.po.dto.*;
import com.qg24.service.ProjectService;
import com.qg24.service.SDKService;
import com.qg24.utils.ServerEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SDKServiceImpl implements SDKService {

    @Autowired
    private SDKMapper sdkMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public boolean connect(int projectId) {
        return sdkMapper.selectProjectById(projectId) != null;
    }

    @Override
    public void batchInsertBackendRequestInfo(List<BackendRequestInfoDTO> backendRequestInfoDTOList) throws Exception {
        if (backendRequestInfoDTOList.isEmpty()) {
            return;
        }
        for (BackendRequestInfoDTO backendRequestInfoDTO : backendRequestInfoDTOList) {
            backendRequestInfoDTO.setProjectId(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getProjectId()));
            backendRequestInfoDTO.setRequestIp(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getRequestIp()));
            backendRequestInfoDTO.setRequestApi(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getRequestApi()));
            backendRequestInfoDTO.setRequestController(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getRequestController()));
            backendRequestInfoDTO.setRequestMethod(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getRequestMethod()));
            backendRequestInfoDTO.setRequestTime(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getRequestTime()));
            backendRequestInfoDTO.setRequestUrl(ServerEncryptUtils.UnEncryptText(backendRequestInfoDTO.getRequestUrl()));
        }
        projectService.updateErrorRate(backendRequestInfoDTOList.size(),Integer.parseInt(backendRequestInfoDTOList.get(0).getProjectId()));
        projectMapper.updateProjectAccessCount(backendRequestInfoDTOList.size(), Integer.parseInt(backendRequestInfoDTOList.get(0).getProjectId()));
        sdkMapper.batchInsertBackendRequestInfo(backendRequestInfoDTOList);
    }

    @Override
    public void batchInsertBackendExceptionInfo(List<BackendExceptionInfoDTO> backendExceptionInfoDTOList) throws Exception {
        if (backendExceptionInfoDTOList.isEmpty()) {
            return;
        }
        for (BackendExceptionInfoDTO backendExceptionInfoDTO : backendExceptionInfoDTOList) {
            backendExceptionInfoDTO.setProjectId(ServerEncryptUtils.UnEncryptText(backendExceptionInfoDTO.getProjectId()));
            backendExceptionInfoDTO.setException(ServerEncryptUtils.UnEncryptText(backendExceptionInfoDTO.getException()));
            backendExceptionInfoDTO.setErrorTime(ServerEncryptUtils.UnEncryptText(backendExceptionInfoDTO.getErrorTime()));
        }
        projectService.updateErrorNumAndRate(backendExceptionInfoDTOList.size(),Integer.parseInt(backendExceptionInfoDTOList.get(0).getProjectId()));
        sdkMapper.batchInsertBackendExceptionInfo(backendExceptionInfoDTOList);
    }

    @Override
    public void batchInsertBackendLog(List<BackendLogDTO> backendLogDTOList) throws Exception {
        if (backendLogDTOList.isEmpty()) {
            return;
        }
        for (BackendLogDTO backendLogDTO : backendLogDTOList) {
            backendLogDTO.setProjectId(ServerEncryptUtils.UnEncryptText(backendLogDTO.getProjectId()));
            backendLogDTO.setLogLevel(ServerEncryptUtils.UnEncryptText(backendLogDTO.getLogLevel()));
            backendLogDTO.setLogTime(ServerEncryptUtils.UnEncryptText(backendLogDTO.getLogTime()));
            backendLogDTO.setLogContent(ServerEncryptUtils.UnEncryptText(backendLogDTO.getLogContent()));
        }
        projectService.updateErrorRate(backendLogDTOList.size(), Integer.parseInt(String.valueOf(backendLogDTOList.get(0).getProjectId())));
        sdkMapper.batchInsertBackendLog(backendLogDTOList);
    }

    @Override
    public void batchInsertFrontPerformanceLog(List<FrontLogDTO> frontLogDTOList) {
        projectService.updateErrorRate(frontLogDTOList.size(), frontLogDTOList.get(0).getProjectId());
        sdkMapper.batchInsertFrontPerformanceLog(frontLogDTOList);
    }

    @Override
    public void batchInsertFrontExceptionLog(List<FrontLogDTO> frontLogDTOList) {
        projectService.updateErrorNumAndRate(frontLogDTOList.size(), frontLogDTOList.get(0).getProjectId());
        sdkMapper.batchInsertFrontExceptionLog(frontLogDTOList);
        projectService.updateFrontErrorNum(frontLogDTOList.size(), frontLogDTOList.get(0).getProjectId());
    }

    @Override
    public void batchInsertMobilePerformanceLog(List<MobilePerformanceLogDTO> mobilePerformanceLogDTOList) {
        projectService.updateErrorRate(mobilePerformanceLogDTOList.size(), (int) mobilePerformanceLogDTOList.get(0).getProjectId());
        sdkMapper.batchInsertMobilePerformanceLog(mobilePerformanceLogDTOList);
    }

    @Override
    public void batchInsertMobileExceptionLog(List<MobileExceptionDTO> mobileExceptionDTOList) {
        projectService.updateErrorNumAndRate(mobileExceptionDTOList.size(), (int) mobileExceptionDTOList.get(0).getProjectId());
        sdkMapper.batchInsertMobileExceptionLog(mobileExceptionDTOList);
        projectService.updateMobileErrorNum(mobileExceptionDTOList.size(), (int) mobileExceptionDTOList.get(0).getProjectId());
    }

}
