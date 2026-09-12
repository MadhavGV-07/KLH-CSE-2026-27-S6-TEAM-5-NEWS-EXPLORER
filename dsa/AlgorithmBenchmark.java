package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark suite runner in Java comparing Naive, KMP, Rabin-Karp,
 * Trie Inverted Index, and Dynamic Programming Edit Distance.
 */
public class AlgorithmBenchmark {

    public static class BenchmarkMetrics {
        public final String algorithm;
        public final long executionTimeUs;
        public final long executionTimeNs;
        public final int comparisons;
        public final int matchesFound;
        public final String timeComplexity;
        public final String spaceComplexity;
        public final String engine;

        public BenchmarkMetrics(String algorithm, long executionTimeUs, long executionTimeNs,
                                int comparisons, int matchesFound,
                                String timeComplexity, String spaceComplexity, String engine) {
            this.algorithm = algorithm;
            this.executionTimeUs = executionTimeUs;
            this.executionTimeNs = executionTimeNs;
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
        long t0 = System.nanoTime();
        NaiveSearch.NaiveResult naive = NaiveSearch.search(text, pattern, false);
        long t1 = System.nanoTime();
        results.add(new BenchmarkMetrics(
            "Naive Search", naive.executionTimeUs, (t1 - t0), naive.comparisonsCount, naive.matches.size(),
            "O(N × M)", "O(1)", "Java 25 Core"
        ));

        // 2. KMP Search
        long t2 = System.nanoTime();
        KmpSearch.KmpResult kmp = KmpSearch.search(text, pattern, false);
        long t3 = System.nanoTime();
        results.add(new BenchmarkMetrics(
            "KMP (Knuth-Morris-Pratt)", kmp.executionTimeUs, (t3 - t2), kmp.comparisonsCount, kmp.matches.size(),
            "O(N + M)", "O(M)", "Java 25 Core"
        ));

        // 3. Rabin-Karp Search
        long t4 = System.nanoTime();
        RabinKarpSearch.RabinKarpResult rk = RabinKarpSearch.search(text, pattern, false);
        long t5 = System.nanoTime();
        results.add(new BenchmarkMetrics(
            "Rabin-Karp (Rolling Hash)", rk.executionTimeUs, (t5 - t4), rk.comparisonsCount, rk.matches.size(),
            "O(N + M) avg", "O(1)", "Java 25 Core"
        ));

        // 4. Trie Search (prefix / inverted index)
        TrieSearch trie = new TrieSearch();
        String[] tokens = text.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            trie.insert(tokens[i].replaceAll("[^a-zA-Z0-9]", ""), "bench-doc", i);
        }
        long t6 = System.nanoTime();
        TrieSearch.TrieSearchResult trieRes = trie.search(pattern);
        long t7 = System.nanoTime();
        results.add(new BenchmarkMetrics(
            "Trie (Prefix Index)", Math.max(1, (t7 - t6) / 1000), (t7 - t6), pattern.length(), trieRes.totalFrequency,
            "O(M)", "O(Σ × M × N)", "Java 25 Core"
        ));

        // 5. Fuzzy Match (Edit Distance DP)
        String sampleTarget = text.substring(0, Math.min(text.length(), Math.max(pattern.length() * 2, 40)));
        long t8 = System.nanoTime();
        FuzzyEditDistance.EditDistanceResult fuzzy = FuzzyEditDistance.compute(pattern, sampleTarget);
        long t9 = System.nanoTime();
        results.add(new BenchmarkMetrics(
            "Levenshtein DP Edit Distance", fuzzy.executionTimeUs, (t9 - t8), fuzzy.comparisonsCount,
            fuzzy.similarityRatio > 0.6 ? 1 : 0, "O(N × M)", "O(N × M)", "Java 25 Core"
        ));

        return results;
    }

    /**
     * Formats benchmark metrics into an ANSI-styled ASCII table.
     */
    public static String formatBenchmarkTable(List<BenchmarkMetrics> metrics, int textLength, int patternLength) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Benchmark Parameters: Text Length = %d chars | Pattern Length = %d chars\n", textLength, patternLength));
        sb.append("┌──────────────────────────────────┬──────────────┬─────────────┬───────────┬──────────────┬──────────────┐\n");
        sb.append("│ Algorithm                        │ Time (μs/ns) │ Comparisons │ Matches   │ Time Compl.  │ Space Compl. │\n");
        sb.append("├──────────────────────────────────┼──────────────┼─────────────┼───────────┼──────────────┼──────────────┤\n");

        for (BenchmarkMetrics m : metrics) {
            String timeStr = m.executionTimeUs > 0 ? (m.executionTimeUs + " μs") : (m.executionTimeNs + " ns");
            sb.append(String.format("│ %-32s │ %-12s │ %-11d │ %-9d │ %-12s │ %-12s │\n",
                    m.algorithm,
                    timeStr,
                    m.comparisons,
                    m.matchesFound,
                    m.timeComplexity,
                    m.spaceComplexity));
        }
        sb.append("└──────────────────────────────────┴──────────────┴─────────────┴───────────┴──────────────┴──────────────┘\n");
        return sb.toString();
    }
}
