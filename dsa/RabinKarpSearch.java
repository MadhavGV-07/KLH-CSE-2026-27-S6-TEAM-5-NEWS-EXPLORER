package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.List;

public class RabinKarpSearch {

    public static final int ALPHABET_BASE = 256;
    public static final long PRIME_MODULO = 1000000007L;

    public static class RabinKarpStepTrace {
        public int stepIndex;
        public int windowStart;
        public String windowText;
        public long windowHash;
        public long patternHash;
        public boolean hashCollision;
        public boolean actualMatch;
        public String action;

        public RabinKarpStepTrace(int stepIndex, int windowStart, String windowText, long windowHash,
                                  long patternHash, boolean hashCollision, boolean actualMatch, String action) {
            this.stepIndex = stepIndex;
            this.windowStart = windowStart;
            this.windowText = windowText;
            this.windowHash = windowHash;
            this.patternHash = patternHash;
            this.hashCollision = hashCollision;
            this.actualMatch = actualMatch;
            this.action = action;
        }
    }

    public static class RabinKarpResult {
        public List<Integer> matches;
        public List<RabinKarpStepTrace> stepTrace;
        public int comparisonsCount;
        public int collisionsCount;
        public long executionTimeUs;
        public long patternHash;

        public RabinKarpResult(List<Integer> matches, List<RabinKarpStepTrace> stepTrace, int comparisonsCount,
                               int collisionsCount, long executionTimeUs, long patternHash) {
            this.matches = matches;
            this.stepTrace = stepTrace;
            this.comparisonsCount = comparisonsCount;
            this.collisionsCount = collisionsCount;
            this.executionTimeUs = executionTimeUs;
            this.patternHash = patternHash;
        }
    }

    /**
     * Executes Rabin-Karp search manually in Java using polynomial rolling hash with step tracing.
     */
    public static RabinKarpResult search(String text, String pattern, boolean caseSensitive) {
        long startTime = System.nanoTime();
        List<Integer> matches = new ArrayList<>();
        List<RabinKarpStepTrace> stepTrace = new ArrayList<>();

        if (text == null || pattern == null || pattern.isEmpty() || text.length() < pattern.length()) {
            return new RabinKarpResult(matches, stepTrace, 0, 0, 0, 0);
        }

        String txt = caseSensitive ? text : text.toLowerCase();
        String pat = caseSensitive ? pattern : pattern.toLowerCase();

        int n = txt.length();
        int m = pat.length();
        int comparisons = 0;
        int collisions = 0;

        // Calculate h = (ALPHABET_BASE^(m-1)) % PRIME_MODULO
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * ALPHABET_BASE) % PRIME_MODULO;
        }

        long pHash = 0;
        long tHash = 0;

        for (int i = 0; i < m; i++) {
            pHash = (ALPHABET_BASE * pHash + pat.charAt(i)) % PRIME_MODULO;
            tHash = (ALPHABET_BASE * tHash + txt.charAt(i)) % PRIME_MODULO;
            comparisons++;
        }

        int stepCount = 0;

        for (int i = 0; i <= n - m; i++) {
            String windowText = text.substring(i, i + m);
            boolean hashCollision = false;
            boolean actualMatch = false;

            if (pHash == tHash) {
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
                    actualMatch = true;
                } else {
                    hashCollision = true;
                    collisions++;
                }
            }

            if (stepCount < 200) {
                String action;
                if (actualMatch) {
                    action = String.format("Hash Match (%d) & Character Match! Match found at index %d", tHash, i);
                } else if (hashCollision) {
                    action = String.format("SPURIOUS HIT! Hash Match (%d) but characters differ!", tHash);
                } else {
                    action = String.format("Hash mismatch (%d vs %d). Sliding window right...", tHash, pHash);
                }

                stepTrace.add(new RabinKarpStepTrace(
                        stepCount + 1,
                        i,
                        windowText,
                        tHash,
                        pHash,
                        hashCollision,
                        actualMatch,
                        action
                ));
                stepCount++;
            }

            if (i < n - m) {
                long leadingVal = txt.charAt(i);
                long trailingVal = txt.charAt(i + m);

                tHash = (ALPHABET_BASE * (tHash - leadingVal * h) + trailingVal) % PRIME_MODULO;

                if (tHash < 0) {
                    tHash += PRIME_MODULO;
                }
            }
        }

        long endTime = System.nanoTime();
        long executionTimeUs = Math.max(1, (endTime - startTime) / 1000);

        return new RabinKarpResult(matches, stepTrace, comparisons, collisions, executionTimeUs, pHash);
    }
}
