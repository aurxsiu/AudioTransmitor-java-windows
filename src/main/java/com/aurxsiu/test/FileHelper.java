package com.aurxsiu.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class FileHelper {
    public static String readString(File file)  throws Exception{
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    /**
     * 不保证成功,也不校验
     * */
    public static void writeString(File file,String result) throws Exception{
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(result.getBytes(StandardCharsets.UTF_8));
        }
    }
}
