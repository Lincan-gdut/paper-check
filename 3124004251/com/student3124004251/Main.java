package com.student3124004251;

public class Main {
    public static void main(String[] args) {
        // 参数数量校验：必须正好 3 个，否则提示用法并退出
        if (args.length != 3) {
            System.err.println("用法: java -jar main.jar <原文> <抄袭版> <答案文件>");
            System.exit(1);
        }
        try {
            // 1. 读取原文和抄袭版文本内容
            String origText = TextFileReader.read(args[0]);
            String copyText = TextFileReader.read(args[1]);
            // 2. 计算相似度（字符级 3-gram + Jaccard）
            double rate = SimilarityChecker.jaccardNgram(origText, copyText, 3);
            // 3. 将结果写出到答案文件（保留两位小数）
            AnswerFileWriter.write(args[2], rate);
            // 4. 控制台同时打印结果，便于调试
            System.out.printf("重复率: %.2f%n", rate);
        } catch (Exception e) {
            // 统一捕获异常：文件不存在、编码错误、IO 失败等
            System.err.println("[错误] " + e.getMessage());
            System.exit(1);
        }
    }
}