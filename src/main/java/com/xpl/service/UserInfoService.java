package com.xpl.service;

import com.xpl.entity.po.UserInfoPO;

import java.util.List;

public interface UserInfoService {

    public UserInfoPO getById(int id);

    public void insertUserInfo(List<UserInfoPO> userInfoPOS);
}
