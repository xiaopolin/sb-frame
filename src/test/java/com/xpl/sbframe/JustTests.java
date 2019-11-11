package com.xpl.sbframe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xpl.api.po.UserInfoPO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JustTests {

    @Test
    public void testFastJson(){
        UserInfoPO userInfoPO = new UserInfoPO();
        System.out.println(userInfoPO);

        JSONObject jsonObject = JSON.parseObject("{1:\"\\x");
        System.out.println(jsonObject);
    }

    @Test
    public void testLong(){
        Long a = 999999999999999999L;
        System.out.println(a.intValue());
    }

    @Test
    public void testList(){
        List<UserInfoPO> userInfoPOS = new ArrayList<>();
        UserInfoPO userInfoPO1 = new UserInfoPO();
        userInfoPO1.setName("aaa");
        userInfoPO1.setPassword("123456");

        userInfoPOS.add(userInfoPO1);
        System.out.println(userInfoPOS);


        userInfoPO1.setName("bbb");
        userInfoPO1.setPassword("456789");
        userInfoPOS.add(userInfoPO1);

        System.out.println(userInfoPOS);
    }

}
