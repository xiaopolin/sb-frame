package com.xpl.dao;

import com.xpl.entity.po.UserInfoPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoDao {

    public UserInfoPO getById(@Param("id") int id);

    public void insertUserInfo(@Param("UserInfoPOS")List<UserInfoPO> userInfoPOS);
}
