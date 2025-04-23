package com.aurxsiu.test;

import jdk.swing.interop.SwingInterOpUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UdpAudioSender {
    public static void test(){
        ByteBuffer buffer = ByteBuffer.allocate(100);
//        buffer.putLong
        System.out.println(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        test();
    }


    public static void send(String host) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress targetAddress = InetAddress.getByName(host); // Android 端 IP
        int targetPort = 20233;

        int sampleRate = 44100;
        int channels = 2;
        int bytesPerSample = 2;
        int frameDurationMs = 10;
        int frameSize = sampleRate * channels * bytesPerSample * frameDurationMs / 1000;

        // 录音设备示例：你可以用 FFmpeg 推送到某个中间缓冲，然后在这里读取
//        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("test.pcm"));
        InputStream stream = getInputStream();
        byte[] frameData = new byte[frameSize];
        int frameSeq = 0;

        while (stream.read(frameData) == frameSize) {
            ByteBuffer buffer = ByteBuffer.allocate(4 + frameSize);
            buffer.putInt(frameSeq++);
            buffer.put(frameData);

            DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.capacity(), targetAddress, targetPort);
            socket.send(packet);
            Thread.sleep(frameDurationMs);
        }
        System.out.println("wrong");

        stream.close();
        socket.close();
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
