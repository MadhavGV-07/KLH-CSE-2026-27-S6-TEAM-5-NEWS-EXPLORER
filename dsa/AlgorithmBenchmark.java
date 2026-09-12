package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark suite runner in Java comparing Naive, KMP, Rabin-Karp, and Fuzzy Edit Distance.
 */
public class AlgorithmBenchmark {

    public static class BenchmarkMetrics {
        public String algorithm;
        public long executionTimeUs;
        public int comparisons;
        public int matchesFound;
        public String timeComplexity;
        public String spaceComplexity;
        public String engine;

        public BenchmarkMetrics(String algorithm, long executionTimeUs, int comparisons, int matchesFound,
                                String timeComplexity, String spaceComplexity, String engine) {
            this.algorithm = algorithm;
            this.executionTimeUs = executionTimeUs;
            this.comparisons = comparisons;
            this.matchesFound = matchesFound;
            this.timeComplexity = timeComplexity;
            this.spaceComplexity = spaceComplexity;
            this.engine = engine;
        }
    }

    public static List<BenchmarkMetrics> runBenchmark(String text, String pattern) {
        List<BenchmarkMetrics> results = new ArrayList<>();
        if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty()) {
            return results;
        }

        // 1. Naive Search
        NaiveSearch.NaiveResult naive = NaiveSearch.search(text, pattern, false);
        results.add(new BenchmarkMetrics(
            "Naive Search", naive.executionTimeUs, naive.comparisonsCount, naive.matches.size(),
            "O(N × M)", "O(1)", "Java 25 Core"
        ));

        // 2. KMP Search
        KmpSearch.KmpResult kmp = KmpSearch.search(text, pattern, false);
        results.add(new BenchmarkMetrics(
            "KMP (Knuth-Morris-Pratt)", kmp.executionTimeUs, kmp.comparisonsCount, kmp.matches.size(),
            "O(N + M)", "O(M)", "Java 25 Core"
        ));

        // 3. Rabin-Karp Search
        RabinKarpSearch.RabinKarpResult rk = RabinKarpSearch.search(text, pattern, false);
        results.add(new BenchmarkMetrics(
            "Rabin-Karp (Rolling Hash)", rk.executionTimeUs, rk.comparisonsCount, rk.matches.size(),
            "O(N + M) avg", "O(1)", "Java 25 Core"
        ));

        // 4. Fuzzy Match (Edit Distance DP)
        String sampleTarget = text.substring(0, Math.min(text.length(), 30));
        FuzzyEditDistance.EditDistanceResult fuzzy = FuzzyEditDistance.compute(pattern, sampleTarget);
        results.add(new BenchmarkMetrics(
            "Fuzzy Match (Edit Distance DP)", fuzzy.executionTimeUs, fuzzy.comparisonsCount,
            fuzzy.similarityRatio > 0.6 ? 1 : 0, "O(N × M)", "O(N × M)", "Java 25 Core"
        ));

        return results;
    }
}
