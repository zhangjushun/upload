package com.cdv.upload.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {
    /**
     * @param path   压缩的文件路径
     * @param format 格式（zip、rar）
     */
    public static void compressFile(String path, String format,String name) throws Exception {
        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("路径：" + path + " 不存在文件，无法压缩！！！");
        }
        // 用于存放压缩文件的文件夹
        //String generateFile = file.getParent() + File.separator +"CompressFile";
        String generateFile = file.getParent();
        File compress = new File(generateFile);
        // 如果文件夹不存在，进行创建
        if( !compress.exists() ){
            compress.mkdirs();
        }
        // 目的压缩文件
        String generateFileName = compress.getAbsolutePath() + File.separator + name + "." + format;
        // 输入流 表示从一个源读取数据
        // 输出流 表示向一个目标写入数据
        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);
        // 压缩输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
        generateFile(zipOutputStream,file,"");
        System.out.println("原文件位置：" + file.getAbsolutePath() + "，压缩文件位置：" + generateFileName);
        // 关闭 输出流
        zipOutputStream.close();
    }

    /**
     * @param out  输出流
     * @param file 目标文件
     * @param dir  文件夹
     * @throws Exception
     */
    private static void generateFile(ZipOutputStream out, File file, String dir) throws Exception {
        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                generateFile(out, files[i], dir + files[i].getName());
            }
        } else {
            // 输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 标记要打包的条目
            out.putNextEntry(new ZipEntry(dir));
            // 进行写操作
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            // 关闭输入流
            inputStream.close();
        }
    }

    /**
     * 原文件位置：C:\Users\17461\Desktop\新建文件夹，压缩文件位置：C:\Users\17461\Desktop\CompressFile\压缩文件_新建文件夹.zip
     * @param args
     */
    public static void main(String[] args) {
        String path = "C:\\Users\\17461\\Desktop\\新建文件夹";
        // 文件压缩的格式
        String format = "zip";
        try {
            compressFile(path, format,"压缩文件_新建文件夹");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}