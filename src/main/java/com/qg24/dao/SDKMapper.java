package com.qg24.dao;

import com.qg24.po.dto.*;
import com.qg24.po.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SDKMapper {

    @Select("select * from project where project_id = #{projectId}")
    Project selectProjectById(@Param("projectId") int projectId);

    void batchInsertBackendRequestInfo(@Param("backendRequestInfoDTOList") List<BackendRequestInfoDTO> backendRequestInfoDTOList);

    void batchInsertBackendExceptionInfo(@Param("backendExceptionInfoDTOList") List<BackendExceptionInfoDTO> backendExceptionInfoDTOList);

    void batchInsertFrontPerformanceLog(@Param("frontLogDTOList") List<FrontLogDTO> frontLogDTOList);

    void batchInsertFrontExceptionLog(@Param("frontLogDTOList") List<FrontLogDTO> frontLogDTOList);

    void batchInsertMobilePerformanceLog(@Param("mobilePerformanceLogDTOList") List<MobilePerformanceLogDTO> mobilePerformanceLogDTOList);

    void batchInsertMobileExceptionLog(@Param("mobileExceptionDTOList") List<MobileExceptionDTO> mobileExceptionDTOList);

    void batchInsertBackendLog(@Param("backendLogDTOList") List<BackendLogDTO> backendLogDTOList);
}
