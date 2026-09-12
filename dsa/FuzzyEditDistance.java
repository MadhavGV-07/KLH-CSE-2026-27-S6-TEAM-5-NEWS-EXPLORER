package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuzzyEditDistance {

    public static class EditDistanceTraceCell {
        public int row;
        public int col;
        public String char1;
        public String char2;
        public String op; // exact, substitute, insert, delete
        public int cost;

        public EditDistanceTraceCell(int row, int col, String char1, String char2, String op, int cost) {
            this.row = row;
            this.col = col;
            this.char1 = char1;
            this.char2 = char2;
            this.op = op;
            this.cost = cost;
        }
    }

    public static class EditDistanceResult {
        public String pattern;
        public String target;
        public int[][] matrix;
        public List<EditDistanceTraceCell> traceback;
        public int distance;
        public double similarityRatio;
        public int comparisonsCount;
        public long executionTimeUs;

        public EditDistanceResult(String pattern, String target, int[][] matrix, List<EditDistanceTraceCell> traceback,
                                  int distance, double similarityRatio, int comparisonsCount, long executionTimeUs) {
            this.pattern = pattern;
            this.target = target;
            this.matrix = matrix;
            this.traceback = traceback;
            this.distance = distance;
            this.similarityRatio = similarityRatio;
            this.comparisonsCount = comparisonsCount;
            this.executionTimeUs = executionTimeUs;
        }
    }

    public static class FuzzyTextMatch {
        public int start;
        public int end;
        public int distance;
        public String text;

        public FuzzyTextMatch(int start, int end, int distance, String text) {
            this.start = start;
            this.end = end;
            this.distance = distance;
            this.text = text;
        }
    }

    public static class FuzzySearchResult {
        public List<FuzzyTextMatch> matches;
        public int comparisonsCount;
        public long executionTimeUs;

        public FuzzySearchResult(List<FuzzyTextMatch> matches, int comparisonsCount, long executionTimeUs) {
            this.matches = matches;
            this.comparisonsCount = comparisonsCount;
            this.executionTimeUs = executionTimeUs;
        }
    }

    /**
     * Computes Levenshtein Edit Distance manually using Dynamic Programming 2D matrix
     * and reconstructs the optimal alignment traceback path.
     */
    public static EditDistanceResult compute(String pattern, String target) {
        long startTime = System.nanoTime();
        String p = pattern.toLowerCase();
        String t = target.toLowerCase();
        int m = p.length();
        int n = t.length();

        int comparisonsCount = 0;

        if (m == 0 && n == 0) {
            return new EditDistanceResult(pattern, target, new int[][]{{0}}, new ArrayList<>(), 0, 1.0, 0, 1);
        }

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                comparisonsCount++;
                int cost = (p.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    dp[i - 1][j] + 1,        // Deletion
                    Math.min(
                        dp[i][j - 1] + 1,    // Insertion
                        dp[i - 1][j - 1] + cost // Substitution / Exact
                    )
                );
            }
        }

        // Traceback path reconstruction
        List<EditDistanceTraceCell> traceback = new ArrayList<>();
        int currI = m;
        int currJ = n;

        while (currI > 0 || currJ > 0) {
            int cellCost = dp[currI][currJ];

            if (currI > 0 && currJ > 0) {
                int matchCost = (p.charAt(currI - 1) == t.charAt(currJ - 1)) ? 0 : 1;

                if (cellCost == dp[currI - 1][currJ - 1] + matchCost) {
                    traceback.add(new EditDistanceTraceCell(
                        currI, currJ,
                        String.valueOf(pattern.charAt(currI - 1)),
                        String.valueOf(target.charAt(currJ - 1)),
                        matchCost == 0 ? "exact" : "substitute",
                        cellCost
                    ));
                    currI--;
                    currJ--;
                    continue;
                }
            }

            if (currI > 0 && cellCost == dp[currI - 1][currJ] + 1) {
                traceback.add(new EditDistanceTraceCell(
                    currI, currJ,
                    String.valueOf(pattern.charAt(currI - 1)),
                    "-",
                    "delete",
                    cellCost
                ));
                currI--;
                continue;
            }

            if (currJ > 0 && cellCost == dp[currI][currJ - 1] + 1) {
                traceback.add(new EditDistanceTraceCell(
                    currI, currJ,
                    "-",
                    String.valueOf(target.charAt(currJ - 1)),
                    "insert",
                    cellCost
                ));
                currJ--;
                continue;
            }
        }

        Collections.reverse(traceback);

        int distance = dp[m][n];
        int maxLen = Math.max(m, n);
        double similarityRatio = (maxLen == 0) ? 1.0 : Math.max(0.0, 1.0 - (double) distance / maxLen);

        long endTime = System.nanoTime();
        long executionTimeUs = Math.max(1, (endTime - startTime) / 1000);

        return new EditDistanceResult(pattern, target, dp, traceback, distance, similarityRatio, comparisonsCount, executionTimeUs);
    }

    /**
     * Scans text word-by-word in Java to find approximate matches within max distance.
     */
    public static FuzzySearchResult fuzzySearchInText(String pattern, String text, int maxDistance) {
        long startTime = System.nanoTime();
        List<FuzzyTextMatch> matches = new ArrayList<>();
        int totalComparisons = 0;

        if (pattern == null || pattern.isEmpty() || text == null || text.isEmpty()) {
            return new FuzzySearchResult(matches, 0, 0);
        }

        String[] words = text.split("\\s+");
        int charPos = 0;

        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "");
            if (!cleanWord.isEmpty()) {
                EditDistanceResult res = compute(pattern, cleanWord);
                totalComparisons += res.comparisonsCount;

                if (res.distance <= maxDistance) {
                    int startIdx = text.indexOf(word, charPos);
                    int s = startIdx >= 0 ? startIdx : charPos;
                    matches.add(new FuzzyTextMatch(s, s + word.length(), res.distance, word));
                }
            }
            charPos += word.length() + 1;
        }

        long endTime = System.nanoTime();
        long executionTimeUs = Math.max(1, (endTime - startTime) / 1000);

        return new FuzzySearchResult(matches, totalComparisons, executionTimeUs);
    }
}
