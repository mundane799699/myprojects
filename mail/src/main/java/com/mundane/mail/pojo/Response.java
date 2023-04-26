package com.mundane.mail.pojo;

import lombok.Data;

@Data
public class Response<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Response success(T data) {
        Response response = new Response<>();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static Response success() {
        Response response = new Response<>();
        response.setCode(200);
        response.setMsg("success");
        return response;
    }

    public static Response fail(String msg) {
        Response response = new Response<>();
        response.setCode(500);
        response.setMsg(msg);
        return response;
    }
}
