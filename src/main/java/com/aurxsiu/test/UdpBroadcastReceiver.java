package com.aurxsiu.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpBroadcastReceiver {

    public static void main(String[] args) {
        final int PORT = 50000;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("正在监听端口：" + PORT);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                String sender = packet.getAddress().getHostAddress();
                System.out.printf("收到来自 %s 的消息：%s%n", sender, msg);
            }

        } catch (SocketException e) {
            System.err.println("端口绑定失败：" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
