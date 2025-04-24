package com.aurxsiu.test;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
            TargetDataLine input = getInputStream();
            System.out.println("音频获取成功!");

            OutputStream socketOut = socket.getOutputStream();

            int samplesPerFrame = SAMPLE_RATE * FRAME_DURATION_MS / 1000;
            int bytesPerSample = SAMPLE_SIZE / 8;
            int maxByteNum = samplesPerFrame * bytesPerSample * CHANNELS;
            byte[] bytes = new byte[maxByteNum];
            while (true) {
                int available = input.available();
                if(available > maxByteNum*100){
                    throw new RuntimeException("延时过高:"+available+":估算延时时间:"+available/maxByteNum*20+"s");
                }
                if(available==0){
                    continue;
                }
                int read = input.read(bytes,0,bytes.length);
                socketOut.write(bytes,0,read);
            }
        }
    }

    static final int SAMPLE_RATE = 44100;     // 采样率 Hz
    static final int CHANNELS = 2;            // 单声道
    static final int SAMPLE_SIZE = 16;        // 位深 16bit
    static final int FRAME_DURATION_MS = 10;  // 每帧持续时间 ms
    private static TargetDataLine getInputStream() throws Exception {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
        mic.open(format);
        mic.start();
        return mic;
    }
}
