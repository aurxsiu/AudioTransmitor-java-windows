package com.aurxsiu.test;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * ffmpeg -f dshow -i audio="CABLE Output (VB-Audio Virtual Cable)" -f s16le -acodec pcm_s16le -ar 44100 -ac 2 -*/

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(20233);
        System.out.println("等待手机连接...");
        Socket socket = serverSocket.accept();
        System.out.println("客户端已连接：" + socket.getRemoteSocketAddress());

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-f", "dshow",
                "-i", "audio=CABLE Output (VB-Audio Virtual Cable)",
                "-f", "s16le",
                "-acodec", "pcm_s16le",
                "-ar", "44100",
                "-ac", "2", "-"
        );
        pb.redirectErrorStream(false);  // 不将错误输出流合并到标准输出流
        pb.redirectError(new File("ffmpeg_error.log"));  // 可以将错误输出到文件

        Process ffmpeg = pb.start();
        InputStream ffmpegOut = ffmpeg.getInputStream();
        OutputStream socketOut = socket.getOutputStream();

        byte[] buffer = new byte[8192];
        int len;
        while ((len = ffmpegOut.read(buffer)) != -1) {
//            System.out.println(new String(buffer));
            socketOut.write(buffer, 0, len);
        }
    }
}
