package com.mundane.mail.pojo;

import lombok.Data;

@Data
public class WeChatRequest {
    private String roomName;

    private String uid;

    private String uname;

    private String word;

    private String roomId;
}
