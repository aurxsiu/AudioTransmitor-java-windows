package com.aurxsiu.test;

import java.io.InputStream;
import java.io.OutputStream;

public class TransmitCenter {
    private final InputStream ffmpegInputStream;
    private final OutputStream netOutputStream;

    private boolean flush = false;

    public TransmitCenter(InputStream ffmpegInputStream, OutputStream netOutputStream) {
        this.ffmpegInputStream = ffmpegInputStream;
        this.netOutputStream = netOutputStream;
    }
    /**
     * todo 实现同步功能{@link FlushInstruction}
     * */
    public void transmit(){
        try{
            byte[] buffer = new byte[8192];
            int len;
            while(true){
                if(!flush){
                    len = ffmpegInputStream.read(buffer);
                    if(len != -1){
                        netOutputStream.write(buffer,0,len);
                    }else{
                        System.out.println("Warn:ffmpeg终止输出,关闭连接");
                        return;
                    }
                }else{
                    ffmpegInputStream.skip(ffmpegInputStream.available());


                    flush = false;
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
