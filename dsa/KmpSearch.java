package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KmpSearch {

    public static class KmpStepTrace {
        public int stepIndex;
        public int textPointer;
        public int patternPointer;
        public String currentTextChar;
        public String currentPatternChar;
        public boolean isMatch;
        public int lpsValue;
        public String action;
        public int[] lpsArrayState;

        public KmpStepTrace(int stepIndex, int textPointer, int patternPointer,
                            String currentTextChar, String currentPatternChar, boolean isMatch,
                            int lpsValue, String action, int[] lpsArrayState) {
            this.stepIndex = stepIndex;
            this.textPointer = textPointer;
            this.patternPointer = patternPointer;
            this.currentTextChar = currentTextChar;
            this.currentPatternChar = currentPatternChar;
            this.isMatch = isMatch;
            this.lpsValue = lpsValue;
            this.action = action;
            this.lpsArrayState = lpsArrayState;
        }
    }

    public static class LpsResult {
        public int[] lps;
        public int comparisons;

        public LpsResult(int[] lps, int comparisons) {
            this.lps = lps;
            this.comparisons = comparisons;
        }
    }

    public static class KmpResult {
        public List<Integer> matches;
        public List<KmpStepTrace> stepTrace;
        public int comparisonsCount;
        public long executionTimeUs;
        public int[] lps;

        public KmpResult(List<Integer> matches, List<KmpStepTrace> stepTrace, int comparisonsCount, long executionTimeUs, int[] lps) {
            this.matches = matches;
            this.stepTrace = stepTrace;
            this.comparisonsCount = comparisonsCount;
            this.executionTimeUs = executionTimeUs;
            this.lps = lps;
        }
    }

    /**
     * Computes Longest Prefix Suffix (LPS) array manually in Java.
     */
    public static LpsResult computeLPSArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        int comparisons = 0;

        while (i < m) {
            comparisons++;
            if (Character.toLowerCase(pattern.charAt(i)) == Character.toLowerCase(pattern.charAt(len))) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return new LpsResult(lps, comparisons);
    }

    /**
     * Executes Knuth-Morris-Pratt search manually in Java with step tracing.
     */
    public static KmpResult search(String text, String pattern, boolean caseSensitive) {
        long startTime = System.nanoTime();
        List<Integer> matches = new ArrayList<>();
        List<KmpStepTrace> stepTrace = new ArrayList<>();

        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new KmpResult(matches, stepTrace, 0, 0, new int[0]);
        }

        String txt = caseSensitive ? text : text.toLowerCase();
        String pat = caseSensitive ? pattern : pattern.toLowerCase();

        LpsResult lpsRes = computeLPSArray(pat);
        int[] lps = lpsRes.lps;
        int n = txt.length();
        int m = pat.length();

        int i = 0; // Index for text
        int j = 0; // Index for pattern
        int totalComparisons = lpsRes.comparisons;
        int stepCount = 0;

        while (i < n) {
            totalComparisons++;
            boolean isCharMatch = txt.charAt(i) == pat.charAt(j);

            if (stepCount < 200) {
                String action;
                if (isCharMatch) {
                    if (j == m - 1) {
                        action = String.format("Full Match Found at index %d!", i - m + 1);
                    } else {
                        action = String.format("Match '%c' at pattern index %d. Advancing pointers.", text.charAt(i), j);
                    }
                } else {
                    if (j > 0) {
                        action = String.format("Mismatch '%c' vs '%c'. Shifting pattern using LPS[%d] = %d.",
                                text.charAt(i), pattern.charAt(j), j - 1, lps[j - 1]);
                    } else {
                        action = String.format("Mismatch '%c' vs '%c'. Incrementing text pointer.",
                                text.charAt(i), pattern.charAt(j));
                    }
                }

                stepTrace.add(new KmpStepTrace(
                        stepCount + 1,
                        i,
                        j,
                        String.valueOf(text.charAt(i)),
                        String.valueOf(pattern.charAt(j)),
                        isCharMatch,
                        j < lps.length ? lps[j] : 0,
                        action,
                        Arrays.copyOf(lps, lps.length)
                ));
                stepCount++;
            }

            if (isCharMatch) {
                i++;
                j++;
            }

            if (j == m) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < n && txt.charAt(i) != pat.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        long endTime = System.nanoTime();
        long executionTimeUs = Math.max(1, (endTime - startTime) / 1000);

        return new KmpResult(matches, stepTrace, totalComparisons, executionTimeUs, lps);
    }
}
