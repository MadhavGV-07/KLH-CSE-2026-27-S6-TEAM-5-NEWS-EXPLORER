package com.newsexplorer.dsa;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Main Java Application Entry Point for News Explorer.
 * Executes pure Java implementations of KMP, Rabin-Karp, Fuzzy Edit Distance,
 * Document Similarity Graph, Suffix Array LCP, Naive Search, and Performance Benchmarks.
 */
public class NewsExplorerApp {

    private static final List<NewsArticle> CORPUS = Arrays.asList(
        new NewsArticle(
            "art-101",
            "Quantum Leap in Artificial Intelligence: Neural Networks Achieves Real-Time Reasoning",
            "Researchers demonstrate a groundbreaking neural network architecture combined with quantum hardware.",
            "In a historic milestone for artificial intelligence, computer scientists at the Quantum AI Research Consortium have unveiled a new hybrid neural network architecture capable of performing real-time logical reasoning over trillion-parameter datasets. By combining Knuth-Morris-Pratt pattern matching algorithms for high-speed token lookup with quantum state superposition, the system achieves sub-millisecond query responses.",
            "AI", "Dr. Elena Rostova", "2026-08-28"
        ),
        new NewsArticle(
            "art-102",
            "Global Economy Faces Shift as Algorithmic High-Frequency Trading Dominates Markets",
            "Financial markets report unprecedented volume as machine learning models...",
            "Financial institutions are overhauling legacy trade execution systems in favor of hyper-optimized algorithms utilizing Rabin-Karp polynomial rolling hashes to monitor arbitrage opportunities across global exchanges. As algorithmic high-frequency trading accounts for over 82% of equity transactions worldwide, regulatory authorities are introducing new real-time auditing frameworks.",
            "Economy", "Marcus Vance", "2026-08-27"
        ),
        new NewsArticle(
            "art-103",
            "Climate Science Breakthrough: Ocean Sensor Networks Detect Deep-Sea Thermal Shifts",
            "Autonomous oceanographic sensor floats deploy advanced pattern detection...",
            "An international climate research initiative has deployed over 5,000 deep-sea autonomous sensor pods across the Atlantic Ocean. Utilizing lightweight, energy-efficient string matching algorithms onboard embedded microcontrollers, these sensors search stream telemetry for sudden thermal spikes and salinity variations.",
            "Climate", "Aria Chen", "2026-08-26"
        ),
        new NewsArticle(
            "art-104",
            "Zero-Day Cyber Threats Defeated by Dynamic Pattern Matching and Graph Neural Networks",
            "Cybersecurity teams implement dynamic fuzzy text matching and edit distance metrics...",
            "Polymorphic malware which constantly alters its byte sequence to evade traditional antivirus signature checks is facing a formidable new opponent. Cybersecurity defense platforms have integrated Levenshtein fuzzy matching and KMP string search into kernel-level memory monitoring engines.",
            "Cybersecurity", "Alexander Wright", "2026-08-25"
        )
    );

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("          NEWS EXPLORER — ADVANCED JAVA 25 DSA ENGINE");
        System.out.println("=========================================================================");
        System.out.println("Executing manual zero-dependency Java implementations of:");
        System.out.println(" - Knuth-Morris-Pratt (KMP) String Search (with Step Tracing)");
        System.out.println(" - Rabin-Karp Polynomial Rolling Hash Search (with Collision Tracking)");
        System.out.println(" - Dynamic Programming Levenshtein Edit Distance (with Traceback Alignment)");
        System.out.println(" - TF-IDF Vector Space & Cosine Similarity Document Graph");
        System.out.println(" - Suffix Array & Kasai's Longest Common Prefix (LCP) Analysis");
        System.out.println(" - Naive Baseline String Search & Algorithm Benchmark Suite");
        System.out.println("=========================================================================\n");

        if (args.length > 0 && "--server".equalsIgnoreCase(args[0])) {
            startJavaServer();
            return;
        }

        runAllJavaDsaExecutions();
    }

    public static void runAllJavaDsaExecutions() {
        String testPattern = "algorithm";
        System.out.println(">>> 1. EXECUTION: Knuth-Morris-Pratt (KMP) Search in Java");
        System.out.println("    Pattern: \"" + testPattern + "\"");
        int totalKmpMatches = 0;
        for (NewsArticle art : CORPUS) {
            KmpSearch.KmpResult res = KmpSearch.search(art.getFullSearchableText(), testPattern, false);
            totalKmpMatches += res.matches.size();
            System.out.printf("    - [%s] Found %d match(es) in %d μs (%d comparisons, %d trace steps)\n",
                art.getId(), res.matches.size(), res.executionTimeUs, res.comparisonsCount, res.stepTrace.size());
        }
        System.out.println("    KMP Total Matches: " + totalKmpMatches + "\n");

        System.out.println(">>> 2. EXECUTION: Rabin-Karp Rolling Hash Search in Java");
        System.out.println("    Pattern: \"" + testPattern + "\"");
        int totalRkMatches = 0;
        for (NewsArticle art : CORPUS) {
            RabinKarpSearch.RabinKarpResult res = RabinKarpSearch.search(art.getFullSearchableText(), testPattern, false);
            totalRkMatches += res.matches.size();
            System.out.printf("    - [%s] Hash: %d | Matches: %d | Collisions: %d | Time: %d μs\n",
                art.getId(), res.patternHash, res.matches.size(), res.collisionsCount, res.executionTimeUs);
        }
        System.out.println("    Rabin-Karp Total Matches: " + totalRkMatches + "\n");

        System.out.println(">>> 3. EXECUTION: Dynamic Programming Levenshtein Edit Distance in Java");
        String p1 = "neural";
        String p2 = "neuron";
        FuzzyEditDistance.EditDistanceResult fuzzyRes = FuzzyEditDistance.compute(p1, p2);
        System.out.printf("    Edit Distance (\"%s\" vs \"%s\"): %d\n", p1, p2, fuzzyRes.distance);
        System.out.printf("    Similarity Ratio: %.2f%%\n", fuzzyRes.similarityRatio * 100);
        System.out.printf("    DP Matrix Dimensions: %dx%d | Traceback Steps: %d\n\n",
                fuzzyRes.matrix.length, fuzzyRes.matrix[0].length, fuzzyRes.traceback.size());

        System.out.println(">>> 4. EXECUTION: TF-IDF & Cosine Similarity Document Graph in Java");
        DocumentSimilarityGraph.SimilarityGraphData graphData = DocumentSimilarityGraph.buildGraph(CORPUS, 0.10);
        System.out.println("    Constructed Document Nodes: " + graphData.nodes.size());
        System.out.println("    Constructed Document Relationship Edges (> 10% similarity):");
        for (DocumentSimilarityGraph.SimilarityEdge edge : graphData.edges) {
            System.out.printf("    - Connection: %s <---> %s (Similarity: %.1f%%, Shared: %s)\n",
                edge.source, edge.target, edge.similarity * 100, String.join(", ", edge.sharedTerms));
        }
        System.out.println();

        System.out.println(">>> 5. EXECUTION: Suffix Array & Kasai's LCP Phrase Analysis in Java");
        String sampleCorpusText = CORPUS.get(0).getContent();
        SuffixArrayLCP.SuffixAnalysisResult suffixRes = SuffixArrayLCP.analyzeCorpusPatterns(sampleCorpusText);
        System.out.println("    Generated Suffix Array length: " + suffixRes.suffixArray.length);
        System.out.println("    First 5 Suffix Offsets: " + Arrays.toString(Arrays.copyOf(suffixRes.suffixArray, Math.min(5, suffixRes.suffixArray.length))));
        System.out.println("    Max LCP Value: " + Arrays.stream(suffixRes.lcpArray).max().orElse(0));
        System.out.println("    Extracted Top Repeated Key Phrases: " + suffixRes.longestRepeatedSubstrings.size() + "\n");

        System.out.println(">>> 6. EXECUTION: Java Algorithm Benchmark Suite");
        List<AlgorithmBenchmark.BenchmarkMetrics> benchmarks = AlgorithmBenchmark.runBenchmark(sampleCorpusText, testPattern);
        for (AlgorithmBenchmark.BenchmarkMetrics bm : benchmarks) {
            System.out.printf("    - %-32s | %6d μs | %6d comps | Complexity: %s\n",
                    bm.algorithm, bm.executionTimeUs, bm.comparisons, bm.timeComplexity);
        }

        System.out.println("\n=========================================================================");
        System.out.println(" [SUCCESS] All Java 25 DSA Core Algorithms executed cleanly!");
        System.out.println("=========================================================================");
    }

    public static void startJavaServer() {
        try {
            NewsExplorerApi.main(new String[0]);
        } catch (IOException e) {
            System.err.println("Error starting Java server: " + e.getMessage());
        }
    }
}
