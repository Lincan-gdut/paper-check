package com.student3124004251;

import java.util.HashSet;
import java.util.Set;

public class SimilarityChecker {

    static String preprocess(String text) {
        // \\s+ 匹配任意连续空白字符
        return text.replaceAll("\\s+", "");
    }

    static Set<String> ngrams(String text, int n) {
        Set<String> set = new HashSet<>();
        // 文本长度不足 n 时，整个文本作为一个 gram（非空时）
        if (text.length() < n) {
            if (!text.isEmpty()) set.add(text);
            return set;
        }
        // 滑动窗口，每次取长度 n 的子串
        for (int i = 0; i <= text.length() - n; i++) {
            set.add(text.substring(i, i + n));
        }
        return set;
    }

    public static double jaccardNgram(String a, String b, int n) {
        String pa = preprocess(a);
        String pb = preprocess(b);
        Set<String> sa = ngrams(pa, n);
        Set<String> sb = ngrams(pb, n);

        // 边界情况：两篇都为空视为完全一致
        if (sa.isEmpty() && sb.isEmpty()) return 1.0;
        // 其中一篇为空则相似度为 0
        if (sa.isEmpty() || sb.isEmpty()) return 0.0;

        // 交集
        Set<String> inter = new HashSet<>(sa);
        inter.retainAll(sb);
        // 并集
        Set<String> union = new HashSet<>(sa);
        union.addAll(sb);

        // Jaccard 系数
        return (double) inter.size() / union.size();
    }
}