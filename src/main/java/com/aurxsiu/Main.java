package com.aurxsiu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        /*if (args.length == 0) {
            printHelp();
            return;
        }

        switch (args[0]) {
            case "install-driver":
                installDriver();
                break;
            case "set-default":
                setDefaultAudioDevice("CABLE Input (VB-Audio Virtual Cable)");
                break;
            case "restore-default":
                restoreDefaultDevice();
                break;
            case "current-device":
                printCurrentDevice();
                break;
            case "start-stream":
                startStreaming();
                break;
            default:
                printHelp();
        }*/
    }

    static void printHelp() {
        System.out.println("""
        可用命令：
          install-driver     安装 VB-CABLE 驱动
          set-default        将 VB-CABLE 设为默认输出
          restore-default    恢复原始输出设备
          current-device     查看当前默认音频设备
          start-stream       启动音频采集和转发
        """);
    }

    static void installDriver() throws IOException {
        System.out.println("正在安装 VB-CABLE 驱动...");
        new ProcessBuilder("cmd", "/c", "scripts\\install_vbcable.bat").inheritIO().start();
    }

    static void setDefaultAudioDevice(String deviceName) throws IOException {
        System.out.println("设置默认音频输出为: " + deviceName);
        new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File",
                "scripts\\set_default_device.ps1", deviceName).inheritIO().start();
    }

    static void restoreDefaultDevice() throws IOException {
        System.out.println("恢复默认音频设备...");
        new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File",
                "scripts\\set_default_device.ps1", "Speakers").inheritIO().start();
    }

    static void printCurrentDevice() throws IOException {
        new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File",
                "scripts\\get_default_device.ps1").inheritIO().start();
    }

    static void startStreaming() throws IOException {
        System.out.println("启动音频采集...");
        new ProcessBuilder("ffmpeg", "-f", "dshow",
                "-i", "audio=VB-Audio Virtual Cable",
                "-f", "s16le",
                "-acodec", "pcm_s16le",
                "-ar", "44100",
                "-ac", "2", "-").inheritIO().start();
    }
}