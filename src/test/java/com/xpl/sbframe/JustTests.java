package com.xpl.sbframe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xpl.api.po.UserInfoPO;
import org.junit.Test;

import java.util.Arrays;

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
        Object[] field = {"a", "b", null};
        System.out.println(Arrays.asList(field).contains("a"));
    }
}
