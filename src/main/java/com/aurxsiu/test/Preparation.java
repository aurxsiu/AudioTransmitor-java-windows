package com.aurxsiu.test;

import com.aurxsiu.test.config.Port;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Preparation {
    public static final String defaultDeviceFileName = "defaultDevice";

    //todo 能够断开连接并重新连接
    public static volatile boolean isConnected = false;

    public static String getDriveByDefault() throws  Exception{
        File file = new File(Resource.getResourcePath()+"/"+defaultDeviceFileName);
        String result = FileHelper.readString(file);
        if (!result.isEmpty()) {
            return result;
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
        File file = new File(Resource.getResourcePath()+"/"+defaultDeviceFileName);
        file.createNewFile();
        FileHelper.writeString(file,result);
        return result;
    }
    //todo 增加windows端选择的权利
    public static void tryConnect() throws  Exception{
        try (DatagramSocket socket = new DatagramSocket(Port.windowsDetectListen.getPort())){
            socket.setBroadcast(true);

            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            String discoveryMessage = "aurxsiuAudioTransmitApp:requireConnect";
            DatagramPacket packet = new DatagramPacket(
                    discoveryMessage.getBytes(),
                    discoveryMessage.length(),
                    broadcastAddress,
                    Port.androidGetDetectedListen.getPort()
            );

            while (!isConnected) {
                socket.send(packet);
                Thread.sleep(1000); // 每秒广播一次
            }
            System.out.println("连接成功");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    public static void tryConnect2() throws Exception{
        while(!isConnected) {
            try (MulticastSocket multicastSocket = new MulticastSocket()) {
                byte[] data = "test".getBytes(StandardCharsets.UTF_8);
                multicastSocket.send(new DatagramPacket(data, data.length, InetAddress.getByName("224.0.0.1"), Port.androidGetDetectedListen.getPort()));
                multicastSocket.close();
                Thread.sleep(1000);
            }
        }
    }
    public static void tryConnect3() throws Exception{
        for (InetAddress listAllBroadcastAddress : listAllBroadcastAddresses()) {
            broadcast("test",listAllBroadcastAddress);
        }
    }
    public static void broadcast(
            String broadcastMessage, InetAddress address) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, Port.androidGetDetectedListen.getPort());
        socket.send(packet);
        socket.close();
    }

    public static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }
    public static void main(String[] args) throws Exception{
        Preparation.tryConnect3();
    }
}
