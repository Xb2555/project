package com.qg24.service;

import com.qg24.po.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface SDKService {

    boolean connect(int projectId);

    void batchInsertBackendRequestInfo(List<BackendRequestInfoDTO> backendRequestInfoDTOList) throws Exception;

    void batchInsertBackendExceptionInfo(List<BackendExceptionInfoDTO> backendExceptionInfoDTOList) throws Exception;

    void batchInsertFrontPerformanceLog(List<FrontLogDTO> frontLogDTOList);

    void batchInsertFrontExceptionLog(List<FrontLogDTO> frontLogDTOList);

    void batchInsertMobilePerformanceLog(List<MobilePerformanceLogDTO> mobilePerformanceLogDTOList);

    void batchInsertMobileExceptionLog(List<MobileExceptionDTO> mobileExceptionDTOList);

    void batchInsertBackendLog(List<BackendLogDTO> backendLogDTOList) throws Exception;
}
