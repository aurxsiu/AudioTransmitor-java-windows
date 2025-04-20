package com.aurxsiu.test.config;


public enum Port {
    windowsDetectListen(25424),
    androidGetDetectedListen(25425),
    audioListen(25426);
    private final int port;

    public int getPort() {
        return port;
    }

    Port(int port) {
        this.port = port;
    }
}


