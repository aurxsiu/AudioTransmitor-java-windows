package com.aurxsiu.test;

import java.net.*;
import java.util.Arrays;

public class UDPSender {

    public static void main(String[] args) {
        try {
            String localIp = "10.29.93.150"; // 电脑的IP地址
            String subnetMask = "255.255.0.0"; // 子网掩码
            String message = "Hello from computer!";
            int port = 25425;

            // 计算广播地址
            InetAddress broadcastAddress = getBroadcastAddress(localIp, subnetMask);

            // 发送UDP广播消息
            sendBroadcast(broadcastAddress, message, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 计算广播地址的方法
    public static InetAddress getBroadcastAddress(String ip, String subnetMask) throws Exception {
        InetAddress localAddress = InetAddress.getByName(ip);
        InetAddress subnetMaskAddress = InetAddress.getByName(subnetMask);

        byte[] ipBytes = localAddress.getAddress();
        byte[] subnetMaskBytes = subnetMaskAddress.getAddress();

        // 计算网络部分
        byte[] networkBytes = new byte[ipBytes.length];
        for (int i = 0; i < ipBytes.length; i++) {
            networkBytes[i] = (byte) (ipBytes[i] & subnetMaskBytes[i]);
        }

        // 广播地址部分是将主机部分设置为全1
        byte[] broadcastBytes = new byte[ipBytes.length];
        for (int i = 0; i < ipBytes.length; i++) {
            broadcastBytes[i] = (byte) (networkBytes[i] | (~subnetMaskBytes[i] & 0xFF));
        }

        // 返回计算出的广播地址
        return InetAddress.getByAddress(broadcastBytes);
    }

    // 发送UDP广播消息的方法
    public static void sendBroadcast(InetAddress broadcastAddress, String message, int port) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);

        socket.send(packet);
        System.out.println("Broadcast sent to: " + broadcastAddress);

        socket.close();
    }
}

