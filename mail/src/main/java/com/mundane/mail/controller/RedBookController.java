package com.mundane.mail.controller;

import com.github.pagehelper.PageInfo;
import com.mundane.mail.pojo.RedBookCollectRequest;
import com.mundane.mail.pojo.Response;
import com.mundane.mail.service.RedBookCollectService;
import com.mundane.mail.vo.RedBookCollectExportExcelVo;
import com.mundane.mail.vo.RedBookCollectExportHtmlVo;
import com.mundane.mail.vo.RedBookDeleteRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/mail/redbook")
public class RedBookController {

    @Autowired
    private RedBookCollectService collectService;

    @PostMapping("/collect/save")
    public String collectReceive(@RequestBody RedBookCollectRequest request) {

        try {
            collectService.save(request);
        } catch (Exception e) {
            log.error("保存收藏发生错误", e);
            return "fail";
        }
        return "success";
    }

    @PostMapping(value = "/collect/exportexcel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadExcel(@RequestBody RedBookCollectExportExcelVo vo, HttpServletResponse response) {
        try {
            collectService.downloadExcel(vo, response);
        } catch (Exception e) {
            log.error("导出收藏至excel发生错误", e);
        }
    }

    @PostMapping("/collect/exportHtml")
    public void downloadHtml(@RequestBody RedBookCollectExportHtmlVo vo, HttpServletResponse response) {
        try {
            collectService.downloadHtml(vo, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/collect/query")
    public Response<PageInfo> query(@RequestParam("userId") String userId,
                                    @RequestParam(value = "displayTitle", required = false) String displayTitle,
                                    @RequestParam(value = "ownerNickname", required = false) String ownerNickname,
                                    @RequestParam("pageNum") Integer pageNum,
                                    @RequestParam("pageSize") Integer pageSize) {
        try {
            return Response.success(collectService.queryByUserId(userId, displayTitle, ownerNickname, pageNum, pageSize));
        } catch (Exception e) {
            log.error("queryByUserId发生错误", e);
            return Response.fail(e.getMessage());
        }
    }

    @PostMapping("/collect/deleteByUserId")
    public Response delete(@RequestBody RedBookDeleteRequestVo vo) {
        try {
            collectService.deleteByUserId(vo.getUserId());
            return Response.success();
        } catch (Exception e) {
            log.error("deleteByUserId发生错误", e);
            return Response.fail(e.getMessage());
        }
    }



}
