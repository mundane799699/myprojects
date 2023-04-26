package com.mundane.mail.utils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class ExportExcelUtil {
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            fileName = new String(fileName.getBytes(), "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
