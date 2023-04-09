package com.mundane.mail.pojo;

import lombok.Data;


@Data
public class WeChatResp<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> WeChatResp<T> success(T data) {
        WeChatResp<T> result = new WeChatResp<>();
        result.setMsg("success");
        result.setCode(200);
        result.setData(data);
        return result;
    }
}
