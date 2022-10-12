package com.cdv.upload.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cdv.upload.conf.ServerConfig;
import com.cdv.upload.conf.UploadConfig;
import com.cdv.upload.dto.AjaxResult;
import com.cdv.upload.file.Compress;
import com.cdv.upload.file.FileUploadUtils;
import com.cdv.upload.file.FileUtils;
import com.cdv.upload.util.Base64;
import com.cdv.upload.util.DateUtils;
import com.cdv.upload.util.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/oper")
public class UploadController {
    @Autowired
    private ServerConfig serverConfig;
    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload/{seq}")
    public AjaxResult uploadFile(MultipartFile file, @PathVariable String seq) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = UploadConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file,seq);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }
    @GetMapping("/getSeq")
    public AjaxResult getSeq(){
       return AjaxResult.success("成功", Seq.getId(Seq.uploadSeqType));
    }
    @PostMapping("/save/{seq}")
    public AjaxResult save(@RequestBody JSONObject jsonObject, @PathVariable String seq) throws Exception {
        String s = JSON.toJSONString(jsonObject);
        System.out.println("------原文："+s);
        String base64 = Base64.setEncryptionBase64(s);
        String path=UploadConfig.getUploadPath()+"/"+DateUtils.datePath()+"/"+seq;
//        File file=new File(UploadConfig.getUploadPath()+DateUtils.datePath()+"/"+seq+"/"+ seq+".txt");
//        file.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(path+"/"+ seq+".txt", false))) {
            bufferedWriter.write(base64);
        }
        Compress.compressFile(path,"rar",seq);
        String url = serverConfig.getUrl() + UploadConfig.getUploadPath()+"/"+DateUtils.datePath()+"/"+ seq+".rar";
        return AjaxResult.success("保存成功",url);
    }
}
