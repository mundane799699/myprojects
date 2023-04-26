package com.mundane.mail.vo;

import lombok.Data;

@Data
public class WeiMiShuVO {
    /**
     * 消息类型：1 文字 2 图片url 3 图片base64 4 url卡片链接
     */
    private Integer type;

    /**
     * 消息内容，如果type为1必填，内容换行使用\n
     */
    private String content;

    /**
     * type 为2，3，4必填，图片地址，或者文件地址，例如png,jpg或者zip，excel都可以，必须是网络地址
     */
    private String url;


    /**
     * type 为4必填，卡片链接的描述
     */
    private String description;


    /**
     * 	type 为4必填，卡片链接的缩略图，网络图片地址
     */
    private String thumbnailUrl;


    /**
     * type 为4必填，卡片标题内容
     */
    private String title;

}
