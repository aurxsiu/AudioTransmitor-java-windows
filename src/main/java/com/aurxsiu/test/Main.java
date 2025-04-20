package com.aurxsiu.test;

import com.aurxsiu.test.config.Port;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ffmpeg -f dshow -i audio="CABLE Output (VB-Audio Virtual Cable)" -f s16le -acodec pcm_s16le -ar 44100 -ac 2 -*/

public class Main {
    //todo 增加设备检测以及端口选择的功能,端口选择依赖设备检测
    public static void main(String[] args) throws Exception {
        /*if(args.length!=1){
            throw new RuntimeException("参数不合法:https://github.com/aurxsiu/AudioTransmitor-java-windows");
        }else{
            String s = args[0];
            switch (s){
                case "s"->{
                    //todo 完善一下
                    Preparation.setDevice();
                }//set
                case "d"->{
                    defaultRun();
                }//default
            }
        }*/

        new Thread(()->{
            try {
                Preparation.tryConnect();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(()->{
            try{
                defaultRun();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }).start();

    }

    public static void defaultRun() throws Exception{
        try (ServerSocket serverSocket = new ServerSocket(Port.audioListen.getPort())) {
            System.out.println("等待手机连接...");
            Socket socket = serverSocket.accept();
            Preparation.isConnected = true;
            System.out.println("客户端已连接：" + socket.getRemoteSocketAddress());
            InputStream ffmpegOut = getInputStream();
            OutputStream socketOut = socket.getOutputStream();

            byte[] buffer = new byte[8192];
            int len;
            while ((len = ffmpegOut.read(buffer)) != -1) {
                socketOut.write(buffer, 0, len);
            }
        }
    }
    private static InputStream getInputStream() throws Exception {
        String device = Preparation.getDriveByDefault();
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-f", "dshow",
                "-i", "audio="+device,
                "-f", "s16le",
                "-acodec", "pcm_s16le",
                "-ar", "44100",
                "-ac", "2", "-"
        );
        pb.redirectErrorStream(false);  // 不将错误输出流合并到标准输出流
        pb.redirectError(new File("ffmpeg_error.log"));  // 可以将错误输出到文件

        Process ffmpeg = pb.start();
        return ffmpeg.getInputStream();
    }
}
