package com.mundane.mail.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mundane.mail.mapper.RedBookCollectMapper;
import com.mundane.mail.pojo.RedBookCollectEntity;
import com.mundane.mail.pojo.RedBookCollectRequest;
import com.mundane.mail.pojo.RedBookCollectResult;
import com.mundane.mail.utils.ExportExcelUtil;
import com.mundane.mail.vo.RedBookCollectExportExcelVo;
import com.mundane.mail.vo.RedBookCollectExportHtmlVo;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.*;

@Service
@Slf4j
public class RedBookCollectService {

    @Autowired
    private RedBookCollectMapper redBookCollectMapper;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    @Transactional(rollbackFor = Exception.class)
    public void save(RedBookCollectRequest request) {
        RedBookCollectResult result = JSONUtil.toBean(request.getResult(), RedBookCollectResult.class);
        List<RedBookCollectEntity> entityList = new ArrayList<>();
        for (RedBookCollectResult.DataBean.NotesBean note : result.getData().getNotes()) {
            RedBookCollectEntity entity = new RedBookCollectEntity();
            entity.setUserId(request.getUserId());
            entity.setNoteId(note.getNoteId());
            entity.setDisplayTitle(note.getDisplayTitle());
            entity.setType(note.getType());
            entity.setCoverUrl(note.getCover().getUrl());
            entity.setOwnerId(note.getUser().getUserId());
            entity.setOwnerAvatar(note.getUser().getAvatar());
            entity.setOwnerNickname(note.getUser().getNickname());
            entity.setLiked(note.getInteractInfo().getLiked());
            entity.setLikedCount(Integer.parseInt(note.getInteractInfo().getLikedCount()));
            entityList.add(entity);
        }
        redBookCollectMapper.batchAddOrUpdate(entityList);
    }

    public void downloadExcel(RedBookCollectExportExcelVo vo, HttpServletResponse response) {
        String[] titles = new String[]{"标题", "笔记链接", "作者", "封面地址", "点赞数"};
        List<RedBookCollectEntity> list = redBookCollectMapper.queryAllByUserId(vo.getUserId());
        //创建Excel文档
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建sheet页
        XSSFSheet sheet = workbook.createSheet("sheet1");
        sheet.setColumnWidth(0, 80 * 256);
        sheet.setColumnWidth(1, 60 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 110 * 256);
        // 冻结第一行
        sheet.createFreezePane(0, 1, 0, 1);
        //创建第一行 通常第一行作为 数据表头
        XSSFRow titleRow = sheet.createRow(0);

        // 创建一个字体对象
        Font boldFont = workbook.createFont();
        boldFont.setBold(true); // 将字体加粗
        // 创建一个单元格样式对象，并将字体对象设置为其中的一部分
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        //设置 第一行的列数据
        for (int i = 0; i < titles.length; i++) {
            XSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(boldStyle);
        }

        CreationHelper createHelper = workbook.getCreationHelper();
        // 创建超链接样式
        CellStyle hlinkStyle = workbook.createCellStyle();
        Font hlinkFont = workbook.createFont();
        hlinkFont.setUnderline(Font.U_SINGLE);
        hlinkFont.setColor(IndexedColors.BLUE.getIndex());
        hlinkStyle.setFont(hlinkFont);

        for (int i = 0; i < list.size(); i++) {
            RedBookCollectEntity entity = list.get(i);
            XSSFRow row = sheet.createRow(i + 1);

            XSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(entity.getDisplayTitle());

            XSSFCell cell1 = row.createCell(1);
            String noteAddress = "https://www.xiaohongshu.com/explore/" + entity.getNoteId();
            Hyperlink link1 = createHelper.createHyperlink(HyperlinkType.URL);
            link1.setAddress(noteAddress);
            cell1.setHyperlink(link1);
            cell1.setCellValue(noteAddress);
            cell1.setCellStyle(hlinkStyle);

            XSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(entity.getOwnerNickname());
            Hyperlink link2 = createHelper.createHyperlink(HyperlinkType.URL);
            link2.setAddress("https://www.xiaohongshu.com/user/profile/" + entity.getOwnerId());
            cell2.setHyperlink(link2);
            cell2.setCellStyle(hlinkStyle);

            XSSFCell cell3 = row.createCell(3);
            String coverUrl = entity.getCoverUrl() + "?imageView2/2/w/640/format/webp";
            Hyperlink link3 = createHelper.createHyperlink(HyperlinkType.URL);
            link3.setAddress(coverUrl);
            cell3.setHyperlink(link3);
            cell3.setCellValue(coverUrl);
            cell3.setCellStyle(hlinkStyle);

            XSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(entity.getLikedCount());
        }

        OutputStream outputStream = null;
        try {
            String fileName = "小红书收藏" + vo.getUserId() + ".xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(outputStream);
        }

    }

    public void downloadHtml(RedBookCollectExportHtmlVo vo, HttpServletResponse response) throws Exception {
        // Create a new file to store the generated HTML
        String fileName = "小红书收藏" + vo.getUserId() + ".html";
        File htmlFile = new File(fileName);
        if (htmlFile.exists()) {
            htmlFile.delete();
            htmlFile.createNewFile();
        }
        // Generate the HTML using Freemarker
        Map<String, Object> model = new HashMap<>();
        List<RedBookCollectEntity> notes = redBookCollectMapper.queryAllByUserId(vo.getUserId());
        for (RedBookCollectEntity note : notes) {
            String type = note.getType();
            if ("normal".equals(type)) {
                note.setType("普通");
            } else if ("video".equals(type)) {
                note.setType("视频");
            }
        }
        model.put("notes", notes);
        Template template = freemarkerConfig.getConfiguration().getTemplate("table.ftl");
        Writer out = new FileWriter(htmlFile);
        template.process(model, out);
        out.close();
        log.info("htmlFile path = {}", htmlFile.getAbsolutePath());

        BufferedInputStream inputStream = FileUtil.getInputStream(htmlFile);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtil.copy(inputStream, outputStream);
        IoUtil.close(inputStream);
        IoUtil.close(outputStream);
    }

    public PageInfo queryByUserId(String userId, String displayTitle, String ownerNickname, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RedBookCollectEntity> notes = redBookCollectMapper.queryByUserId(userId, displayTitle, ownerNickname);
        for (RedBookCollectEntity note : notes) {
            String type = note.getType();
            if ("normal".equals(type)) {
                note.setType("普通");
            } else if ("video".equals(type)) {
                note.setType("视频");
            }
        }
        PageInfo<RedBookCollectEntity> info = new PageInfo<>(notes);
        return info;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("userId不能为空");
        }
        redBookCollectMapper.deleteByUserId(userId);
    }


}
