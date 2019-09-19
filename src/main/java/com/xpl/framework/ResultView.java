package com.xpl.framework;

import lombok.Data;

@Data
public class ResultView<T> extends BaseEntity {

    private int code;
    private String msg;
    private T data;

}
