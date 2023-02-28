package com.mundane.mail.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Weather {

    private Integer minTemperature;

    private Integer maxTemperature;

    private String weather;

    private String date;
}
