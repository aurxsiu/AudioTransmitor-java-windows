package com.aurxsiu.test;

public class BroadMessage {
    private final String target;
    private final String source;
    private final int port;

    public BroadMessage(String target, String source, int port) {
        this.target = target;
        this.source = source;
        this.port = port;
    }

    public String getTarget() {
        return target;
    }

    public String getSource() {
        return source;
    }

    public int getPort() {
        return port;
    }
}
