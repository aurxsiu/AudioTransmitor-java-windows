package com.aurxsiu.test;

import org.apache.commons.validator.routines.InetAddressValidator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * ffmpeg -f dshow -i audio="CABLE Output (VB-Audio Virtual Cable)" -f s16le -acodec pcm_s16le -ar 44100 -ac 2 -*/

public class Main {
    //todo 增加设备检测以及端口选择的功能,端口选择依赖设备检测
    public static void main(String[] args) throws Exception {

            defaultRun();

    }

    public static void defaultRun() throws Exception{


        try (ServerSocket serverSocket = new ServerSocket(20233)) {
            System.out.println("等待手机连接...");
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接：" + socket.getRemoteSocketAddress());
            InputStream ffmpegOut = getInputStream();
            System.out.println("音频获取成功!");

            OutputStream socketOut = socket.getOutputStream();

            /*byte[] buffer = new byte[8192];
            int len;
            while ((len = ffmpegOut.read(buffer)) != -1) {
                socketOut.write(buffer, 0, len);
            }*/
            Thread.sleep(2000);
            /*
            * 32768是通过实际采样获取的2s的available()大小
            * */
            int maxByteNum = 327680;
            byte[] bytes = new byte[maxByteNum];
            while (true) {
                int available = ffmpegOut.available();
                System.out.println(available);
                if(available > maxByteNum){
                    throw new RuntimeException("延时过高:"+available+":估算延时时间:"+available/maxByteNum*20+"s");
                }
                if(available==0){
                    continue;
                }
                int read = ffmpegOut.read(bytes,0,available);
                socketOut.write(bytes,0,read);
            }
        }
    }

    static final int SAMPLE_RATE = 44100;     // 采样率 Hz
    static final int CHANNELS = 1;            // 单声道
    static final int SAMPLE_SIZE = 16;        // 位深 16bit
    private static TargetDataLine getInputStream() throws Exception {
        /*String device = Preparation.getDriveByDefault();
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
        return ffmpeg.getInputStream();*/
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
        mic.open(format);
        mic.start();
        return mic;
    }


    public static class ScanThread implements Runnable {
        private Scanner scanner = new Scanner(System.in);
        @Override
        public void run() {
            while (true){
                System.out.print(">");
                String get = scanner.nextLine();

            }

        }
    }
}
