package com.student3124004251;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextFileReader {
    public static String read(String path) throws IOException {
        // 文件存在性校验，避免后续读取时报更晦涩的错误
        if (!new File(path).isFile()) {
            throw new FileNotFoundException("文件不存在: " + path);
        }
        // 一次性读入字节数组，再按编码转换
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        // 先按 UTF-8 尝试解码
        String text = new String(bytes, StandardCharsets.UTF_8);
        // UTF-8 解码失败时会产生 U+FFFD 替换字符，此时回退为 GBK
        if (text.contains("\uFFFD")) {
            text = new String(bytes, "GBK");
        }
        return text;
    }
}