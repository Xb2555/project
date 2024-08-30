package com.qg24.dao;

import com.qg24.po.entity.WarningMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WarningMessageMapper {
    int insertMessage(WarningMessage warningMessage);

    int checkMessage(@Param("projectId")int projectId, @Param("flag")int flag);

    WarningMessage selectNewMessageByProjectId(@Param("projectId")int projectId);

    int deleteMessage(@Param("projectId")int projectId);
}
