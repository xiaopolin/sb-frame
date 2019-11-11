package com.xpl.service.ServiceImpl;

import com.xpl.api.po.UserInfoPO;
import com.xpl.dao.UserInfoDao;
import com.xpl.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfoPO getById(int id) {
        UserInfoPO userInfoPO = userInfoDao.getById(id);
        return userInfoPO;
    }

    @Override
    public void insertUserInfo(List<UserInfoPO> userInfoPOS) {
        userInfoDao.insertUserInfo(userInfoPOS);
    }
}
