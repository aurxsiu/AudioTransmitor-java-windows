package com.aurxsiu.test;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;



public class Preparation {
    public static final String defaultDeviceFileName = "defaultDevice";
    public static String getDriveByDefault() throws  Exception{
        File file = new File(System.getProperty("user.dir").replace("\\","/")+"/"+defaultDeviceFileName);
        if(file.isFile()){
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                String result = new String(fileInputStream.readAllBytes(),StandardCharsets.UTF_8);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return setDevice();
    }
    public static String setDevice() throws Exception{
            /*ffmpeg -list_devices true -f dshow -i dummy */
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-list_devices","true",
                "-f","dshow",
                "-i","dummy"
        );
        pb.redirectErrorStream(true);  // 不将错误输出流合并到标准输出流
        try {
            Process ffmpeg = pb.start();
            InputStream ffmpegOut = ffmpeg.getInputStream();
            String s = new String(ffmpegOut.readAllBytes());
            System.out.println(s);
        }catch (Exception e) {
            System.out.println("命令可能出错,可通过ffmpeg查询设备名称");
        }
        System.out.println("输入设备名:");
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        File file = new File(System.getProperty("user.dir").replace("\\","/")+"/"+defaultDeviceFileName);
        file.createNewFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(result.getBytes(StandardCharsets.UTF_8));
        }
        return result;
    }
    public static void main(String[] args) throws Exception{
        Preparation.getDriveByDefault();
    }
}
