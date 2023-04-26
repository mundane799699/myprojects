package com.mundane.mail.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_pic_info")
public class Picture {
    @Id
    private Long id;
    private String url;
    private String title;
    private Integer status;

}
