package com.xpl.sbframe;

import com.alibaba.fastjson.JSONObject;
import com.xpl.api.po.UserInfoPO;
import com.xpl.framework.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SbFrameApplicationTests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;



    @Test
    public void contextLoads() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testRedis(){
        UserInfoPO userInfoPO = new UserInfoPO();
//        redisTemplate.opsForValue().set("b", userInfoPO);

        Map<String , Object> map = new HashMap<>();
        map.put("1", userInfoPO);
        map.put("2", userInfoPO);
        map.put("3", userInfoPO);

        //long add = redisTemplate.opsForSet().add("e", 123, 321);
        List<String> list = new ArrayList<>();
        list.add("8");
        list.add("7");
        list.add("6");
        Object z = redisTemplate.opsForList().remove("z", -1, "6");



        System.out.println("flag:" + z);




    }




}
