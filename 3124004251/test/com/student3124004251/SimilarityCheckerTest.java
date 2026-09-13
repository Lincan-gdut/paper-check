package com.student3124004251;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * 针对 {@link SimilarityChecker}、{@link TextFileReader}、
 * {@link AnswerFileWriter} 的单元测试类。
 * <p>
 * 共 15 个用例，覆盖：相似度边界（相同/不同/部分/空）、
 * 预处理、n-gram 切分、对称性、文件读写、异常处理、输出格式等。
 *
 * @author 3124004251
 */
public class SimilarityCheckerTest {

    // ==================== SimilarityChecker 测试 ====================

    /** 两段文本完全相同，相似度应为 1.0 */
    @Test
    public void testIdentical() {
        assertEquals(1.0, SimilarityChecker.jaccardNgram(
                "今天是星期天", "今天是星期天", 3), 0.001);
    }

    /** 两段文本完全不同，相似度应为 0.0 */
    @Test
    public void testCompletelyDifferent() {
        assertEquals(0.0, SimilarityChecker.jaccardNgram(
                "甲乙丙丁戊", "子丑寅卯辰", 3), 0.001);
    }

    /** 部分相似（增删改）时，相似度应严格介于 0 和 1 之间 */
    @Test
    public void testPartial() {
        double s = SimilarityChecker.jaccardNgram(
                "今天是星期天，天气晴", "今天是周天，天气晴朗", 3);
        assertTrue(s > 0 && s < 1);
    }

    /** 两段都为空字符串时，视为完全一致 */
    @Test
    public void testEmptyBoth() {
        assertEquals(1.0, SimilarityChecker.jaccardNgram("", "", 3), 0.001);
    }

    /** 一篇为空、一篇非空时，相似度为 0 */
    @Test
    public void testEmptyOne() {
        assertEquals(0.0, SimilarityChecker.jaccardNgram("abc", "", 3), 0.001);
    }

    /** 预处理应移除所有空白字符 */
    @Test
    public void testPreprocess() {
        assertEquals("甲乙丙丁", SimilarityChecker.preprocess("甲 乙\n丙\t丁"));
    }

    /** n-gram 基本切分测试 */
    @Test
    public void testNgramsBasic() {
        assertTrue(SimilarityChecker.ngrams("甲乙丙丁", 3).contains("甲乙丙"));
        assertTrue(SimilarityChecker.ngrams("甲乙丙丁", 3).contains("乙丙丁"));
    }

    /** 文本短于 n 时，只返回一个 gram */
    @Test
    public void testNgramsShort() {
        assertEquals(1, SimilarityChecker.ngrams("ab", 3).size());
    }

    /** 空文本应返回空集合 */
    @Test
    public void testNgramsEmpty() {
        assertEquals(0, SimilarityChecker.ngrams("", 3).size());
    }

    /** Jaccard 相似度具有对称性：sim(a,b) == sim(b,a) */
    @Test
    public void testSymmetric() {
        double ab = SimilarityChecker.jaccardNgram("今天天气好", "今天天气不错", 2);
        double ba = SimilarityChecker.jaccardNgram("今天天气不错", "今天天气好", 2);
        assertEquals(ab, ba, 0.0001);
    }

    // ==================== TextFileReader 测试 ====================

    /** 文件不存在时应抛出 FileNotFoundException */
    @Test
    public void testReadFileNotExist() {
        try {
            TextFileReader.read("不存在的文件_xyz.txt");
            fail("应当抛出 FileNotFoundException");
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }

    /** 正常读取 UTF-8 文件，内容应原样返回 */
    @Test
    public void testReadFileNormal() throws Exception {
        File tmp = File.createTempFile("test_read_", ".txt");
        tmp.deleteOnExit();
        // Java 8 兼容写法：使用 OutputStreamWriter + FileOutputStream
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        Files.newOutputStream(tmp.toPath()), StandardCharsets.UTF_8))) {
            pw.print("你好世界");
        }
        assertEquals("你好世界", TextFileReader.read(tmp.getAbsolutePath()));
    }

    // ==================== AnswerFileWriter 测试 ====================

    /** 写出的文件应保留两位小数 */
    @Test
    public void testWriteAnswerFormat() throws Exception {
        File tmp = File.createTempFile("test_ans_", ".txt");
        tmp.deleteOnExit();
        AnswerFileWriter.write(tmp.getAbsolutePath(), 0.5678);
        // Java 8 兼容：用 Files.readAllBytes 读取文件内容
        String content = new String(Files.readAllBytes(tmp.toPath()),
                StandardCharsets.UTF_8);
        assertEquals("0.57", content);
    }

    /** 写出整数边界值 1.0 时应显示为 "1.00" */
    @Test
    public void testWriteAnswerOne() throws Exception {
        File tmp = File.createTempFile("test_ans1_", ".txt");
        tmp.deleteOnExit();
        AnswerFileWriter.write(tmp.getAbsolutePath(), 1.0);
        String content = new String(Files.readAllBytes(tmp.toPath()),
                StandardCharsets.UTF_8);
        assertEquals("1.00", content);
    }

    /** 写出 0 时应显示为 "0.00" */
    @Test
    public void testWriteAnswerZero() throws Exception {
        File tmp = File.createTempFile("test_ans0_", ".txt");
        tmp.deleteOnExit();
        AnswerFileWriter.write(tmp.getAbsolutePath(), 0.0);
        String content = new String(Files.readAllBytes(tmp.toPath()),
                StandardCharsets.UTF_8);
        assertEquals("0.00", content);
    }
}
