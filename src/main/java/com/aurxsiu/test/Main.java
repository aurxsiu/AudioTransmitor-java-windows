package com.aurxsiu.test;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {


        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-f", "dshow",
                "-i", "audio=VB-Audio Virtual Cable",
                "-f", "s16le",
                "-acodec", "pcm_s16le",
                "-ar", "44100",
                "-ac", "2", "-"
        );
        pb.redirectErrorStream(true);
        Process ffmpeg = pb.start();
        InputStream audioInput = ffmpeg.getInputStream();
        Socket client;
        try (ServerSocket server = new ServerSocket(20232)) {
            client = server.accept();
            System.out.println("lianjie");

            OutputStream out = client.getOutputStream();
            audioInput.transferTo(out);
        }
    }
    public static void play(InputStream input) throws LineUnavailableException, IOException {
        // 配置音频格式：16位、44100Hz、立体声、小端序
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(info);
        speakers.open(format);
        speakers.start();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            speakers.write(buffer, 0, bytesRead);
        }

        speakers.drain();
        speakers.close();
    }
}
