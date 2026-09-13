package com.student3124004251;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class AnswerFileWriter {
    public static void write(String path, double value) throws IOException {
        // 使用 try-with-resources 自动关闭流，避免内存泄漏
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path), StandardCharsets.UTF_8))) {
            // %.2f 保证保留两位小数
            pw.printf("%.2f", value);
        }
    }
}