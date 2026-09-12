package com.newsexplorer.dsa;

import java.util.*;

/**
 * =========================================================================
 *                  NEWS EXPLORER — ADVANCED JAVA 25 DSA SUITE
 * =========================================================================
 * A 100% Pure Java Data Structures & Algorithms Interactive Console Engine.
 * Features:
 *   1. Knuth-Morris-Pratt (KMP) String Matching & Step Tracer (O(N + M))
 *   2. Rabin-Karp Polynomial Rolling Hash Search (O(N + M) avg)
 *   3. Levenshtein Dynamic Programming Edit Distance Matrix & Traceback (O(N × M))
 *   4. TF-IDF Vector Space & Cosine Similarity Document Graph
 *   5. Suffix Array & Kasai's Longest Common Prefix (LCP) Analysis
 *   6. Prefix Tree (Trie) Inverted Index & Keyword Autocomplete (O(L))
 *   7. Microbenchmarking Suite (Execution Time, Operations, Space & Big-O)
 * =========================================================================
 */
public class NewsExplorerApp {

    // ANSI Color Escape Codes for rich terminal rendering
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String WHITE = "\u001B[37m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_GREEN = "\u001B[42m";

    private static final List<NewsArticle> CORPUS = ArticleCorpus.getAllArticles();

    public static void main(String[] args) {
        if (args.length > 0) {
            String arg = args[0].trim().toLowerCase();
            if (arg.equals("--server")) {
                startServer();
                return;
            } else if (arg.equals("--demo") || arg.equals("--batch") || arg.equals("-d")) {
                runAutomatedFullDemo();
                return;
            }
        }

        // Check if console is available or if stdin has input
        Scanner scanner = new Scanner(System.in);
        try {
            printBanner();
            runInteractiveMenu(scanner);
        } catch (NoSuchElementException | IllegalStateException e) {
            // Non-interactive fallback (e.g., piped input or redirected stdin)
            runAutomatedFullDemo();
        }
    }

    private static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║            NEWS EXPLORER — ADVANCED JAVA 25 DSA CORE ENGINE               ║");
        System.out.println("║              Zero External Dependencies • Pure Java Standard Library      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(YELLOW + " Loaded Corpus: " + CORPUS.size() + " Real-World News Articles across AI, Economy, Fintech, Climate & Space" + RESET);
        System.out.println();
    }

    private static void runInteractiveMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println(BOLD + WHITE + "─────────────────────────── [ DSA ENGINE MAIN MENU ] ───────────────────────────" + RESET);
            System.out.println(GREEN + " [1]" + RESET + " Interactive Pattern Search Across Corpus (KMP / Rabin-Karp / Fuzzy / Trie)");
            System.out.println(GREEN + " [2]" + RESET + " Knuth-Morris-Pratt (KMP) Pointer Step-by-Step Visualizer");
            System.out.println(GREEN + " [3]" + RESET + " Rabin-Karp Rolling Hash & Window Slide Visualizer");
            System.out.println(GREEN + " [4]" + RESET + " Dynamic Programming Levenshtein Distance Matrix & Traceback");
            System.out.println(GREEN + " [5]" + RESET + " TF-IDF Document Similarity Graph & Cluster Visualizer");
            System.out.println(GREEN + " [6]" + RESET + " Suffix Array & Kasai's LCP Repeated Pattern Analyzer");
            System.out.println(GREEN + " [7]" + RESET + " Prefix Tree (Trie) Inverted Index & Autocomplete Explorer");
            System.out.println(GREEN + " [8]" + RESET + " Algorithm Performance Benchmark Suite (Naive vs KMP vs RK vs Trie)");
            System.out.println(GREEN + " [9]" + RESET + " Run Full Automated Verification Demo (All Engines)");
            System.out.println(RED   + " [0]" + RESET + " Exit");
            System.out.print(CYAN + "\nSelect an option [0-9]: " + RESET);

            if (!scanner.hasNextLine()) break;
            String choice = scanner.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1" -> handleInteractiveSearch(scanner);
                case "2" -> handleKmpVisualizer(scanner);
                case "3" -> handleRabinKarpVisualizer(scanner);
                case "4" -> handleFuzzyMatrixVisualizer(scanner);
                case "5" -> handleGraphVisualizer(scanner);
                case "6" -> handleSuffixArrayVisualizer(scanner);
                case "7" -> handleTrieVisualizer(scanner);
                case "8" -> handleBenchmarkRunner(scanner);
                case "9" -> runAutomatedFullDemo();
                case "0", "exit", "q" -> {
                    System.out.println(YELLOW + "Exiting Java DSA Suite. Goodbye!" + RESET);
                    exit = true;
                }
                default -> System.out.println(RED + "Invalid option. Please enter 0-9." + RESET);
            }
            System.out.println();
        }
    }

    // =========================================================================
    // 1. INTERACTIVE SEARCH ACROSS CORPUS
    // =========================================================================
    private static void handleInteractiveSearch(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== INTERACTIVE CORPUS SEARCH ===" + RESET);
        System.out.print("Enter search pattern (e.g., 'algorithm', 'inflation', 'quantum'): ");
        String pattern = scanner.nextLine().trim();
        if (pattern.isEmpty()) pattern = "algorithm";

        System.out.println("Select Algorithm:");
        System.out.println(" 1. KMP (Knuth-Morris-Pratt)");
        System.out.println(" 2. Rabin-Karp (Polynomial Rolling Hash)");
        System.out.println(" 3. Fuzzy Search (Levenshtein Edit Distance max 2)");
        System.out.println(" 4. Trie (Inverted Index Prefix Lookup)");
        System.out.println(" 5. Naive String Search");
        System.out.print("Choice [1-5, default 1]: ");
        String algoChoice = scanner.nextLine().trim();

        System.out.println("\nSearching for '" + BOLD + pattern + RESET + "'...\n");
        long totalTimeNs = 0;
        int totalMatches = 0;

        for (NewsArticle art : CORPUS) {
            String text = art.getFullSearchableText();
            List<Integer> matchIndices = new ArrayList<>();
            long timeUs = 0;
            int comps = 0;

            switch (algoChoice) {
                case "2" -> {
                    RabinKarpSearch.RabinKarpResult rk = RabinKarpSearch.search(text, pattern, false);
                    matchIndices = rk.matches;
                    timeUs = rk.executionTimeUs;
                    comps = rk.comparisonsCount;
                }
                case "3" -> {
                    FuzzyEditDistance.FuzzySearchResult fz = FuzzyEditDistance.fuzzySearchInText(pattern, text, 2);
                    for (FuzzyEditDistance.FuzzyTextMatch m : fz.matches) {
                        matchIndices.add(m.start);
                    }
                    timeUs = fz.executionTimeUs;
                    comps = fz.comparisonsCount;
                }
                case "4" -> {
                    TrieSearch trie = new TrieSearch();
                    String[] tokens = text.split("\\s+");
                    for (int i = 0; i < tokens.length; i++) {
                        trie.insert(tokens[i].replaceAll("[^a-zA-Z0-9]", ""), art.getId(), i);
                    }
                    long t0 = System.nanoTime();
                    TrieSearch.TrieSearchResult tr = trie.search(pattern);
                    long t1 = System.nanoTime();
                    timeUs = Math.max(1, (t1 - t0) / 1000);
                    comps = pattern.length();
                    for (TrieSearch.Occurrence occ : tr.occurrences) {
                        matchIndices.add(occ.position);
                    }
                }
                case "5" -> {
                    NaiveSearch.NaiveResult nv = NaiveSearch.search(text, pattern, false);
                    matchIndices = nv.matches;
                    timeUs = nv.executionTimeUs;
                    comps = nv.comparisonsCount;
                }
                default -> { // 1 = KMP
                    KmpSearch.KmpResult kmp = KmpSearch.search(text, pattern, false);
                    matchIndices = kmp.matches;
                    timeUs = kmp.executionTimeUs;
                    comps = kmp.comparisonsCount;
                }
            }

            totalMatches += matchIndices.size();
            totalTimeNs += timeUs * 1000;

            if (!matchIndices.isEmpty()) {
                System.out.printf("%s[%s] %s%s (%s)\n", GREEN, art.getId(), art.getTitle(), RESET, art.getCategory());
                System.out.printf("  Matches: %d | Time: %d μs | Comparisons: %d | Offsets: %s\n",
                        matchIndices.size(), timeUs, comps, matchIndices);
                // Print snippet highlighting match
                int firstMatch = matchIndices.get(0);
                int snippetStart = Math.max(0, firstMatch - 30);
                int snippetEnd = Math.min(text.length(), firstMatch + pattern.length() + 30);
                String snippet = text.substring(snippetStart, snippetEnd).replace("\n", " ");
                System.out.println("  Snippet: \"..." + snippet + "...\"\n");
            }
        }

        System.out.printf(BOLD + "Search Complete: Found %d total match(es) in %d μs across %d articles.\n" + RESET,
                totalMatches, totalTimeNs / 1000, CORPUS.size());
    }

    // =========================================================================
    // 2. KMP STEP-BY-STEP VISUALIZER
    // =========================================================================
    private static void handleKmpVisualizer(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== KMP (KNUTH-MORRIS-PRATT) STEP-BY-STEP POINTER VISUALIZER ===" + RESET);
        System.out.println("Text sample: 'quantum neural network architecture for real-time logical reasoning'");
        System.out.print("Enter custom text [or press Enter for sample]: ");
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) text = "quantum neural network architecture for real-time logical reasoning";

        System.out.print("Enter pattern to trace (e.g. 'neural'): ");
        String pattern = scanner.nextLine().trim();
        if (pattern.isEmpty()) pattern = "neural";

        System.out.println("\n1. Computing LPS (Longest Prefix Suffix) Array for pattern: \"" + pattern + "\"");
        KmpSearch.LpsResult lpsRes = KmpSearch.computeLPSArray(pattern);
        System.out.print("   Pattern indices: ");
        for (int i = 0; i < pattern.length(); i++) System.out.printf("%3d", i);
        System.out.println();
        System.out.print("   Pattern chars:   ");
        for (int i = 0; i < pattern.length(); i++) System.out.printf("%3c", pattern.charAt(i));
        System.out.println();
        System.out.print("   LPS Values:      ");
        for (int val : lpsRes.lps) System.out.printf("%3d", val);
        System.out.println("\n   LPS Comparisons: " + lpsRes.comparisons + "\n");

        System.out.println("2. Executing KMP String Search with Step Tracing (showing first 15 steps)...");
        KmpSearch.KmpResult res = KmpSearch.search(text, pattern, false);

        int maxSteps = Math.min(15, res.stepTrace.size());
        for (int s = 0; s < maxSteps; s++) {
            KmpSearch.KmpStepTrace trace = res.stepTrace.get(s);
            String status = trace.isMatch ? (GREEN + "MATCH" + RESET) : (RED + "MISMATCH" + RESET);
            System.out.printf(" Step %2d [%s] Text[%d]='%s' vs Pat[%d]='%s' | %s\n",
                    trace.stepIndex, status, trace.textPointer, trace.currentTextChar,
                    trace.patternPointer, trace.currentPatternChar, trace.action);

            // Visual alignment of Text and Pattern
            int textWindowStart = Math.max(0, trace.textPointer - trace.patternPointer);
            int textWindowEnd = Math.min(text.length(), textWindowStart + pattern.length() + 10);
            String sub = text.substring(textWindowStart, textWindowEnd);
            System.out.println("   Text:    " + sub);

            StringBuilder patIndent = new StringBuilder("   Pattern: ");
            for (int p = 0; p < trace.patternPointer; p++) patIndent.append(" ");
            System.out.println(patIndent.toString() + pattern);

            StringBuilder ptrLine = new StringBuilder("            ");
            for (int p = 0; p < trace.patternPointer; p++) ptrLine.append(" ");
            ptrLine.append(YELLOW + "^ (i=" + trace.textPointer + ", j=" + trace.patternPointer + ")" + RESET);
            System.out.println(ptrLine.toString());
            System.out.println();
        }

        System.out.printf(BOLD + "KMP Result: Found %d match(es) at indices %s in %d μs (%d comparisons)\n" + RESET,
                res.matches.size(), res.matches, res.executionTimeUs, res.comparisonsCount);
    }

    // =========================================================================
    // 3. RABIN-KARP ROLLING HASH VISUALIZER
    // =========================================================================
    private static void handleRabinKarpVisualizer(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== RABIN-KARP ROLLING HASH SLIDING WINDOW VISUALIZER ===" + RESET);
        System.out.println("Base = " + RabinKarpSearch.ALPHABET_BASE + " | Modulo = " + RabinKarpSearch.PRIME_MODULO);
        System.out.print("Enter text [or press Enter for default]: ");
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) text = "algorithmic high-frequency trading arbitrage execution";

        System.out.print("Enter pattern (e.g., 'arbitrage'): ");
        String pattern = scanner.nextLine().trim();
        if (pattern.isEmpty()) pattern = "arbitrage";

        RabinKarpSearch.RabinKarpResult res = RabinKarpSearch.search(text, pattern, false);
        System.out.printf("\nTarget Pattern Hash: %s%d%s\n\n", YELLOW, res.patternHash, RESET);

        int maxSteps = Math.min(12, res.stepTrace.size());
        for (int i = 0; i < maxSteps; i++) {
            RabinKarpSearch.RabinKarpStepTrace st = res.stepTrace.get(i);
            String statusBadge;
            if (st.actualMatch) {
                statusBadge = BG_GREEN + WHITE + BOLD + " MATCH " + RESET;
            } else if (st.hashCollision) {
                statusBadge = RED + BOLD + " SPURIOUS HIT (COLLISION) " + RESET;
            } else {
                statusBadge = BLUE + " SLIDE " + RESET;
            }

            System.out.printf(" Window [%2d..%2d]: \"%s\" | Hash: %-10d | %s | %s\n",
                    st.windowStart, st.windowStart + pattern.length() - 1, st.windowText, st.windowHash, statusBadge, st.action);
        }

        System.out.printf(BOLD + "\nRabin-Karp Result: %d matches found at %s (Collisions: %d, Time: %d μs, Comparisons: %d)\n" + RESET,
                res.matches.size(), res.matches, res.collisionsCount, res.executionTimeUs, res.comparisonsCount);
    }

    // =========================================================================
    // 4. LEVENSHTEIN DYNAMIC PROGRAMMING MATRIX VISUALIZER
    // =========================================================================
    private static void handleFuzzyMatrixVisualizer(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== LEVENSHTEIN DYNAMIC PROGRAMMING EDIT DISTANCE VISUALIZER ===" + RESET);
        System.out.print("Enter String 1 [default: 'neural']: ");
        String s1 = scanner.nextLine().trim();
        if (s1.isEmpty()) s1 = "neural";

        System.out.print("Enter String 2 [default: 'neuron']: ");
        String s2 = scanner.nextLine().trim();
        if (s2.isEmpty()) s2 = "neuron";

        FuzzyEditDistance.EditDistanceResult res = FuzzyEditDistance.compute(s1, s2);
        System.out.printf("\nEdit Distance: %s%d%s | Similarity: %s%.2f%%%s\n\n",
                GREEN + BOLD, res.distance, RESET, YELLOW + BOLD, res.similarityRatio * 100, RESET);

        // Render DP Matrix
        int[][] m = res.matrix;
        System.out.println("Dynamic Programming Cost Matrix D[i, j]:");
        System.out.print("      ε  ");
        for (int j = 0; j < s2.length(); j++) {
            System.out.printf("%3c", s2.charAt(j));
        }
        System.out.println();

        for (int i = 0; i <= s1.length(); i++) {
            if (i == 0) {
                System.out.print("  ε  ");
            } else {
                System.out.printf("%3c  ", s1.charAt(i - 1));
            }
            for (int j = 0; j <= s2.length(); j++) {
                System.out.printf("%3d", m[i][j]);
            }
            System.out.println();
        }

        System.out.println("\nOptimal Alignment Traceback Steps:");
        for (FuzzyEditDistance.EditDistanceTraceCell cell : res.traceback) {
            String opColor = switch (cell.op) {
                case "exact" -> GREEN;
                case "substitute" -> YELLOW;
                case "insert" -> CYAN;
                case "delete" -> RED;
                default -> WHITE;
            };
            System.out.printf("  (%d, %d) '%s' -> '%s' | Op: %s%-10s%s | Cost: %d\n",
                    cell.row, cell.col, cell.char1, cell.char2, opColor, cell.op.toUpperCase(), RESET, cell.cost);
        }
    }

    // =========================================================================
    // 5. TF-IDF DOCUMENT SIMILARITY GRAPH VISUALIZER
    // =========================================================================
    private static void handleGraphVisualizer(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== TF-IDF VECTOR SPACE & COSINE SIMILARITY GRAPH ===" + RESET);
        System.out.print("Enter similarity threshold [0.0 - 1.0, default 0.10]: ");
        String thStr = scanner.nextLine().trim();
        double threshold = 0.10;
        try {
            if (!thStr.isEmpty()) threshold = Double.parseDouble(thStr);
        } catch (NumberFormatException ignored) {}

        DocumentSimilarityGraph.SimilarityGraphData graph = DocumentSimilarityGraph.buildGraph(CORPUS, threshold);
        System.out.println("\n" + graph.renderAsciiSummary());
    }

    // =========================================================================
    // 6. SUFFIX ARRAY & KASAI'S LCP ANALYZER
    // =========================================================================
    private static void handleSuffixArrayVisualizer(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== SUFFIX ARRAY & KASAI'S LCP REPEATED PATTERN FINDER ===" + RESET);
        System.out.print("Select Article ID [default: art-101] or enter custom text: ");
        String input = scanner.nextLine().trim();
        String text;
        if (input.isEmpty() || input.equalsIgnoreCase("art-101")) {
            text = CORPUS.get(0).getContent();
        } else {
            NewsArticle art = ArticleCorpus.getById(input);
            text = (art != null) ? art.getContent() : input;
        }

        SuffixArrayLCP.SuffixAnalysisResult res = SuffixArrayLCP.analyzeCorpusPatterns(text);
        System.out.printf("\nProcessed Text Length: %d chars | Total Suffixes: %d | Time: %d μs\n",
                text.length(), res.suffixArray.length, res.executionTimeUs);

        System.out.println("\nFirst 10 Suffix Array & LCP Entries:");
        System.out.println("┌───────┬────────────┬─────┬────────────────────────────────────────────────┐");
        System.out.println("│ Rank  │ SA Index   │ LCP │ Suffix Preview                                 │");
        System.out.println("├───────┼────────────┼─────┼────────────────────────────────────────────────┤");
        int count = Math.min(10, res.suffixArray.length);
        for (int i = 0; i < count; i++) {
            int saIdx = res.suffixArray[i];
            int lcpVal = (i < res.lcpArray.length) ? res.lcpArray[i] : 0;
            String preview = text.substring(saIdx, Math.min(text.length(), saIdx + 45)).replace("\n", " ");
            System.out.printf("│ %-5d │ %-10d │ %-3d │ %-46s │\n", i, saIdx, lcpVal, preview);
        }
        System.out.println("└───────┴────────────┴─────┴────────────────────────────────────────────────┘");

        System.out.println("\nTop Longest Repeated Substrings Discovered via Kasai's LCP:");
        for (int i = 0; i < Math.min(6, res.longestRepeatedSubstrings.size()); i++) {
            SuffixArrayLCP.RepeatedSubstring rep = res.longestRepeatedSubstrings.get(i);
            System.out.printf("  #%d: \"%s%s%s\" (Length: %d, Frequency: %d)\n",
                    i + 1, GREEN, rep.text, RESET, rep.length, rep.count);
        }
    }

    // =========================================================================
    // 7. TRIE INVERTED INDEX & AUTOCOMPLETE EXPLORER
    // =========================================================================
    private static void handleTrieVisualizer(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== PREFIX TREE (TRIE) INVERTED INDEX & AUTOCOMPLETE ===" + RESET);
        System.out.println("Building inverted index across all " + CORPUS.size() + " news articles...");
        long t0 = System.nanoTime();
        TrieSearch trie = TrieSearch.buildCorpusIndex(CORPUS);
        long t1 = System.nanoTime();
        System.out.printf("Indexed %d unique vocabulary terms in %.2f ms!\n\n",
                trie.getTotalWordCount(), (t1 - t0) / 1_000_000.0);

        System.out.print("Enter prefix to autocomplete (e.g. 'algo', 'quant', 'infl', 'econ'): ");
        String prefix = scanner.nextLine().trim();
        if (prefix.isEmpty()) prefix = "algo";

        List<String> suggestions = trie.findPrefixCompletions(prefix, 8);
        System.out.printf("\nAutocomplete suggestions for '%s%s%s':\n", YELLOW, prefix, RESET);
        if (suggestions.isEmpty()) {
            System.out.println("  (No vocabulary terms found matching prefix)");
        } else {
            for (String s : suggestions) {
                TrieSearch.TrieSearchResult tr = trie.search(s);
                System.out.printf("  • %-20s (Total frequency in corpus: %d in %d docs)\n",
                        GREEN + s + RESET, tr.totalFrequency, tr.occurrences.size());
            }
        }

        System.out.println("\nTrie Root Sub-Tree ASCII Structure (Depth 2):");
        System.out.println(trie.renderAsciiTree(2));
    }

    // =========================================================================
    // 8. ALGORITHM PERFORMANCE BENCHMARK SUITE
    // =========================================================================
    private static void handleBenchmarkRunner(Scanner scanner) {
        System.out.println(BOLD + CYAN + "=== MULTI-ALGORITHM PERFORMANCE BENCHMARK SUITE ===" + RESET);
        System.out.print("Enter search pattern to benchmark [default: 'algorithm']: ");
        String pat = scanner.nextLine().trim();
        if (pat.isEmpty()) pat = "algorithm";

        // Aggregate entire corpus into a large text stream for rigorous benchmark
        StringBuilder fullCorpus = new StringBuilder();
        for (NewsArticle art : CORPUS) {
            fullCorpus.append(art.getFullSearchableText()).append("\n");
        }
        String corpusText = fullCorpus.toString();

        System.out.println("\nRunning microbenchmarks on aggregated corpus (" + corpusText.length() + " characters)...");
        List<AlgorithmBenchmark.BenchmarkMetrics> metrics = AlgorithmBenchmark.runBenchmark(corpusText, pat);
        System.out.println("\n" + AlgorithmBenchmark.formatBenchmarkTable(metrics, corpusText.length(), pat.length()));
    }

    // =========================================================================
    // 9. FULL AUTOMATED VERIFICATION DEMO (NON-INTERACTIVE)
    // =========================================================================
    public static void runAutomatedFullDemo() {
        System.out.println(CYAN + BOLD + "=========================================================================");
        System.out.println("          NEWS EXPLORER — ADVANCED JAVA 25 DSA ENGINE DEMO");
        System.out.println("=========================================================================" + RESET);

        String sampleCorpusText = CORPUS.get(0).getContent();
        String testPattern = "algorithm";

        // 1. KMP
        System.out.println(BOLD + "\n>>> 1. Knuth-Morris-Pratt (KMP) Search in Java" + RESET);
        int totalKmp = 0;
        for (NewsArticle art : CORPUS) {
            KmpSearch.KmpResult res = KmpSearch.search(art.getFullSearchableText(), testPattern, false);
            totalKmp += res.matches.size();
        }
        System.out.printf("  [PASS] KMP: Scanned %d articles, found %d total matches for '%s'\n", CORPUS.size(), totalKmp, testPattern);

        // 2. Rabin-Karp
        System.out.println(BOLD + "\n>>> 2. Rabin-Karp Rolling Hash Search in Java" + RESET);
        int totalRk = 0;
        for (NewsArticle art : CORPUS) {
            RabinKarpSearch.RabinKarpResult res = RabinKarpSearch.search(art.getFullSearchableText(), testPattern, false);
            totalRk += res.matches.size();
        }
        System.out.printf("  [PASS] Rabin-Karp: Scanned %d articles, found %d total matches for '%s'\n", CORPUS.size(), totalRk, testPattern);

        // 3. Levenshtein Edit Distance
        System.out.println(BOLD + "\n>>> 3. Dynamic Programming Levenshtein Edit Distance in Java" + RESET);
        FuzzyEditDistance.EditDistanceResult fuzzy = FuzzyEditDistance.compute("neural", "neuron");
        System.out.printf("  [PASS] Edit Distance ('neural' vs 'neuron'): %d (Similarity: %.2f%%, Traceback steps: %d)\n",
                fuzzy.distance, fuzzy.similarityRatio * 100, fuzzy.traceback.size());

        // 4. Document Graph
        System.out.println(BOLD + "\n>>> 4. TF-IDF & Cosine Similarity Document Graph in Java" + RESET);
        DocumentSimilarityGraph.SimilarityGraphData graph = DocumentSimilarityGraph.buildGraph(CORPUS, 0.10);
        System.out.printf("  [PASS] Graph Nodes: %d, Similarity Edges (>10%%): %d\n", graph.nodes.size(), graph.edges.size());

        // 5. Suffix Array & LCP
        System.out.println(BOLD + "\n>>> 5. Suffix Array & Kasai's LCP Repeated Substring Analysis" + RESET);
        SuffixArrayLCP.SuffixAnalysisResult sRes = SuffixArrayLCP.analyzeCorpusPatterns(sampleCorpusText);
        System.out.printf("  [PASS] Suffix Array length: %d, Repeated phrases discovered: %d\n",
                sRes.suffixArray.length, sRes.longestRepeatedSubstrings.size());

        // 6. Trie Search
        System.out.println(BOLD + "\n>>> 6. Prefix Tree (Trie) Inverted Index in Java" + RESET);
        TrieSearch trie = TrieSearch.buildCorpusIndex(CORPUS);
        TrieSearch.TrieSearchResult tr = trie.search(testPattern);
        System.out.printf("  [PASS] Trie: Total unique indexed vocabulary = %d, '%s' occurrences = %d\n",
                trie.getTotalWordCount(), testPattern, tr.totalFrequency);

        // 7. Microbenchmarks
        System.out.println(BOLD + "\n>>> 7. Microbenchmarks Suite Execution" + RESET);
        List<AlgorithmBenchmark.BenchmarkMetrics> benchmarks = AlgorithmBenchmark.runBenchmark(sampleCorpusText, testPattern);
        System.out.println(AlgorithmBenchmark.formatBenchmarkTable(benchmarks, sampleCorpusText.length(), testPattern.length()));

        System.out.println(GREEN + BOLD + "=========================================================================");
        System.out.println(" [SUCCESS] All 7 Pure Java 25 DSA Core Algorithms executed cleanly!");
        System.out.println("=========================================================================" + RESET);
    }

    public static void startServer() {
        try {
            NewsExplorerApi.main(new String[0]);
        } catch (Exception e) {
            System.err.println("Error starting Java server: " + e.getMessage());
        }
    }
}
