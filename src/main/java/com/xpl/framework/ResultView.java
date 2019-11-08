package com.xpl.framework;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class ResultView<T> extends BaseEntity {

    private int code;
    private String msg;
    private T data;

    public ResultView(){
    }

    public ResultView(int code){
        this.code = code;
    }

    public ResultView(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResultView(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
