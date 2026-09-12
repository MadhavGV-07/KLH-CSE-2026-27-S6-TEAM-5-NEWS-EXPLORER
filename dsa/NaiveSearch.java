package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive String Search algorithm implementation in Java for baseline comparison.
 * Time Complexity: O(N * M)
 * Space Complexity: O(1)
 */
public class NaiveSearch {

    public static class NaiveResult {
        public List<Integer> matches;
        public int comparisonsCount;
        public long executionTimeUs;

        public NaiveResult(List<Integer> matches, int comparisonsCount, long executionTimeUs) {
            this.matches = matches;
            this.comparisonsCount = comparisonsCount;
            this.executionTimeUs = executionTimeUs;
        }
    }

    public static NaiveResult search(String text, String pattern, boolean caseSensitive) {
        long startTime = System.nanoTime();
        List<Integer> matches = new ArrayList<>();

        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new NaiveResult(matches, 0, 0);
        }

        String txt = caseSensitive ? text : text.toLowerCase();
        String pat = caseSensitive ? pattern : pattern.toLowerCase();

        int n = txt.length();
        int m = pat.length();
        int comparisons = 0;

        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                comparisons++;
                if (txt.charAt(i + j) != pat.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                matches.add(i);
            }
        }

        long endTime = System.nanoTime();
        long executionTimeUs = Math.max(1, (endTime - startTime) / 1000);

        return new NaiveResult(matches, comparisons, executionTimeUs);
    }
}
