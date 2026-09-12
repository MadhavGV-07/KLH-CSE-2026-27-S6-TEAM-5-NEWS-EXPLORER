package com.newsexplorer.dsa;

import java.util.*;

public class SuffixArrayLCP {

    public static class SuffixItem {
        public int index;
        public String suffix;

        public SuffixItem(int index, String suffix) {
            this.index = index;
            this.suffix = suffix;
        }
    }

    public static class RepeatedSubstring {
        public String text;
        public int count;
        public int length;

        public RepeatedSubstring(String text, int count, int length) {
            this.text = text;
            this.count = count;
            this.length = length;
        }
    }

    public static class SuffixAnalysisResult {
        public int[] suffixArray;
        public int[] lcpArray;
        public List<RepeatedSubstring> longestRepeatedSubstrings;
        public long executionTimeUs;

        public SuffixAnalysisResult(int[] suffixArray, int[] lcpArray,
                                    List<RepeatedSubstring> longestRepeatedSubstrings, long executionTimeUs) {
            this.suffixArray = suffixArray;
            this.lcpArray = lcpArray;
            this.longestRepeatedSubstrings = longestRepeatedSubstrings;
            this.executionTimeUs = executionTimeUs;
        }
    }

    public static int[] buildSuffixArray(String text) {
        int n = text.length();
        SuffixItem[] suffixes = new SuffixItem[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixItem(i, text.substring(i));
        }

        Arrays.sort(suffixes, Comparator.comparing(a -> a.suffix));

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }

    public static int[] computeLCPArray(String text, int[] sa) {
        int n = text.length();
        int[] lcp = new int[n];
        int[] invSA = new int[n];

        for (int i = 0; i < n; i++) {
            invSA[sa[i]] = i;
        }

        int k = 0;
        for (int i = 0; i < n; i++) {
            if (invSA[i] == n - 1) {
                k = 0;
                continue;
            }

            int j = sa[invSA[i] + 1];

            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }

            lcp[invSA[i]] = k;

            if (k > 0) {
                k--;
            }
        }
        return lcp;
    }

    /**
     * Analyzes news corpus text in Java to extract top longest repeated key phrases using Suffix Array & Kasai's LCP.
     */
    public static SuffixAnalysisResult analyzeCorpusPatterns(String text) {
        long startTime = System.nanoTime();
        if (text == null) text = "";
        String cleanedText = text.replaceAll("\\s+", " ").trim();

        if (cleanedText.length() < 5) {
            return new SuffixAnalysisResult(new int[0], new int[0], Collections.emptyList(), 0);
        }

        int[] suffixArray = buildSuffixArray(cleanedText);
        int[] lcpArray = computeLCPArray(cleanedText, suffixArray);

        Map<String, Integer> patternCountMap = new HashMap<>();

        for (int i = 0; i < lcpArray.length; i++) {
            int len = lcpArray[i];
            if (len >= 6) {
                String matchText = cleanedText.substring(suffixArray[i], suffixArray[i] + len).trim();
                if (matchText.matches("^[a-zA-Z0-9\\s,.-]+$") && matchText.split(" ").length >= 2) {
                    patternCountMap.put(matchText, patternCountMap.getOrDefault(matchText, 1) + 1);
                }
            }
        }

        List<RepeatedSubstring> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : patternCountMap.entrySet()) {
            list.add(new RepeatedSubstring(entry.getKey(), entry.getValue(), entry.getKey().length()));
        }

        list.sort((a, b) -> Integer.compare(b.length, a.length));

        List<RepeatedSubstring> topRepeated = list.size() > 8 ? list.subList(0, 8) : list;

        long endTime = System.nanoTime();
        long executionTimeUs = Math.max(1, (endTime - startTime) / 1000);

        return new SuffixAnalysisResult(suffixArray, lcpArray, topRepeated, executionTimeUs);
    }
}
