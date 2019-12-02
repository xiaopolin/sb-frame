package com.xpl.web.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.xpl.entity.constant.CosConstant;
import com.xpl.entity.po.UserInfoPO;
import com.xpl.entity.constant.ErrorCodeConstant;
import com.xpl.framework.ResultView;
import com.xpl.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping(value = "exclude")
    public ResultView<?> exclude(UserInfoPO userInfoPO){
        ResultView<?> result = new ResultView<>();

        log.trace("trace日志打印");
        log.debug("debug日志打印");
        log.info("info日志打印");
        log.warn("warn日志打印");
        log.error("error日志打印");
        result.setCode(ErrorCodeConstant.CODE_SUCCESS);
        result.setMsg("日志已打印！！！");
        return result;
    }

    @GetMapping(value = "user/{id}")
    public ResultView<UserInfoPO> getUser(@PathVariable("id") int id, UserInfoPO userInfoPO, String test){
        ResultView<UserInfoPO> result = new ResultView<>();

        result.setData(userInfoService.getById(id));
        return result;
    }

    @PostMapping(value = "user")
    public ResultView<?> insertUser(){

        List<UserInfoPO> userInfoPOS = new ArrayList<>();
        UserInfoPO userInfoPO1 = new UserInfoPO();
        userInfoPO1.setName("aaa");
        userInfoPO1.setPassword("123456");

        userInfoPOS.add(userInfoPO1);

        UserInfoPO userInfoPO2 = new UserInfoPO();
        userInfoPO2.setName("bbb");
        userInfoPO2.setPassword("456789");
        userInfoPOS.add(userInfoPO2);

        userInfoService.insertUserInfo(userInfoPOS);

        return new ResultView<>(ErrorCodeConstant.CODE_SUCCESS);
    }

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public ResultView<String> uploadFile(String fileName, MultipartFile file){
        ResultView<String> result = new ResultView<>();

        COSCredentials cred = new BasicCOSCredentials(CosConstant.SECRET_ID, CosConstant.SECRET_KEY);
        Region region = new Region(CosConstant.REGION_NAME);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosclient = new COSClient(cred, clientConfig);

        String cosKey = "test/" + fileName;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = null;
        String realUrl = "";
        try {
            putObjectRequest = new PutObjectRequest(CosConstant.BUCKET, cosKey, file.getInputStream(), objectMetadata);
            cosclient.putObject(putObjectRequest);

            Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
            URL url = cosclient.generatePresignedUrl(CosConstant.BUCKET, cosKey, expiration);
            log.info("图片在COS服务器上的url:" + url);
            String urlStr = url.toString();
            realUrl = urlStr.substring(0, urlStr.indexOf("?sign="));
            log.info("实际截取后的路径为：" + realUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

        cosclient.shutdown();

        result.setCode(ErrorCodeConstant.CODE_SUCCESS);
        result.setData(realUrl);
        return result;
    }


}
