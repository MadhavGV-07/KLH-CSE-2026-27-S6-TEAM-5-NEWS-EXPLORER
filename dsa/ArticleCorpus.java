package com.newsexplorer.dsa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory repository of realistic news articles for text processing,
 * pattern matching, indexing, and document graph operations.
 */
public class ArticleCorpus {

    private static final List<NewsArticle> ARTICLES = new ArrayList<>();

    static {
        ARTICLES.add(new NewsArticle(
            "art-101",
            "Quantum Leap in Artificial Intelligence: Neural Networks Achieves Real-Time Reasoning",
            "Researchers demonstrate a groundbreaking neural network architecture combined with quantum hardware, achieving instantaneous pattern recognition and dynamic reasoning across massive datasets.",
            "In a historic milestone for artificial intelligence, computer scientists at the Quantum AI Research Consortium have unveiled a new hybrid neural network architecture capable of performing real-time logical reasoning over trillion-parameter datasets. By combining Knuth-Morris-Pratt pattern matching algorithms for high-speed token lookup with quantum state superposition, the system achieves sub-millisecond query responses. The breakthrough relies heavily on optimized string processing and graph neural networks to index non-linear document relationships. Early benchmarks show a 400x speedup in parsing complex technical documentation and unstructured global news streams. This innovation opens the door to autonomous research assistants capable of synthesizing insights across millions of medical journals, financial ledgers, and global media feeds in real-time.",
            "AI",
            "Dr. Elena Rostova",
            "2026-08-28"
        ));

        ARTICLES.add(new NewsArticle(
            "art-102",
            "Global Economy Faces Shift as Algorithmic High-Frequency Trading Dominates Markets",
            "Financial markets around the globe report unprecedented volume as machine learning models and rolling hash pattern matching algorithms take control of institutional liquidity.",
            "Financial institutions are overhauling legacy trade execution systems in favor of hyper-optimized algorithms utilizing Rabin-Karp polynomial rolling hashes to monitor arbitrage opportunities across global exchanges. As algorithmic high-frequency trading accounts for over 82% of equity transactions worldwide, regulatory authorities are introducing new real-time auditing frameworks. Economists note that while market liquidity has reached historic highs, systemic volatility during rapid news cycles requires advanced fuzzy matching and natural language sentiment analysis to prevent flash crashes. Dynamic programming algorithms now compute edit distances between breaking news announcements and market baseline expectations, allowing automated market makers to re-hedge risk within microseconds of public releases.",
            "Economy",
            "Marcus Vance",
            "2026-08-27"
        ));

        ARTICLES.add(new NewsArticle(
            "art-103",
            "Climate Science Breakthrough: Ocean Sensor Networks Detect Deep-Sea Thermal Shifts",
            "Autonomous oceanographic sensor floats deploy advanced pattern detection to track thermal anomalies and deep-sea current shifts across Atlantic abyssal channels.",
            "An international climate research initiative has deployed over 5,000 deep-sea autonomous sensor pods across the Atlantic Ocean. Utilizing lightweight, energy-efficient string matching algorithms onboard embedded microcontrollers, these sensors search stream telemetry for sudden thermal spikes and salinity variations. By using suffix arrays and longest common prefix computations, researchers can index decades of ocean temperature logs to detect emerging pattern matches across vast geographic zones. The latest findings indicate a subtle shift in the Atlantic Meridional Overturning Circulation, prompting environmental agencies to recalibrate predictive sea-level rise models. Scientists stress the crucial role of efficient algorithms in compressing and analyzing multi-terabyte marine datasets streamed via satellite link.",
            "Climate",
            "Aria Chen",
            "2026-08-26"
        ));

        ARTICLES.add(new NewsArticle(
            "art-104",
            "Zero-Day Cyber Threats Defeated by Dynamic Pattern Matching and Graph Neural Networks",
            "Cybersecurity teams implement dynamic fuzzy text matching and edit distance metrics to detect morphing malware payloads before execution.",
            "Polymorphic malware which constantly alters its byte sequence to evade traditional antivirus signature checks is facing a formidable new opponent. Cybersecurity defense platforms have integrated Levenshtein fuzzy matching and KMP string search into kernel-level memory monitoring engines. By computing the edit distance between suspicious instruction sequences and known vulnerability exploit signatures, security systems can flag malicious code even when attackers obfuscate payloads with variable renaming or dead code insertion. Furthermore, graph centrality algorithms map corporate network traffic flows to isolate compromised nodes within seconds, preventing ransomware propagation across enterprise infrastructures.",
            "Cybersecurity",
            "Alexander Wright",
            "2026-08-25"
        ));

        ARTICLES.add(new NewsArticle(
            "art-105",
            "James Webb Telescope Uncovers Atmospheric Biomarkers on Nearby Exoplanet",
            "Spectroscopic data from exoplanet K2-18b reveals distinct chemical absorption lines, confirmed using randomized spectral matching algorithms.",
            "Astronomers analyzing light spectra gathered by the space telescope have detected strong signatures of methane and carbon dioxide in the atmosphere of exoplanet K2-18b, located 120 light-years from Earth. To separate faint atmospheric signals from background stellar noise, researchers employed Rabin-Karp hash matching and fast Fourier transform spectral cross-correlation. The manual algorithmic pipeline verified recurring spectral absorption patterns against a lab-tested chemical database containing millions of molecular fingerprints. This discovery provides compelling evidence of a hydrogen-rich atmosphere overlying a candidate water ocean, marking one of the most promising leads in the search for habitable worlds beyond our solar system.",
            "Space",
            "Dr. Samuel Sterling",
            "2026-08-24"
        ));

        ARTICLES.add(new NewsArticle(
            "art-106",
            "Next-Generation Semiconductors Harness Graphene for 100GHz Clock Speeds",
            "Engineers achieve a silicon-graphene hybrid processor capable of executing complex text search and graph algorithms at unprecedented clock frequencies.",
            "Hardware architects have successfully fabricated a 2-nanometer hybrid silicon-graphene microprocessor operating reliably at clock speeds exceeding 100 GHz. Designed specifically for data-intensive text processing and graph database operations, the chip features dedicated hardware acceleration blocks for Knuth-Morris-Pratt string comparison and matrix-based dynamic programming. Preliminary tests show that string matching operations that previously required CPU cluster distribution can now be completed directly on-chip with minimal power consumption. Tech analysts predict this architecture will revolutionize cloud search engines, real-time news aggregation services, and automated language translation systems over the next decade.",
            "Tech",
            "Kaitlyn Vance",
            "2026-08-23"
        ));

        ARTICLES.add(new NewsArticle(
            "art-107",
            "RBI MPC Retains Repo Rate at 6.50% Citing Resilient Growth and Food Inflation Vigilance",
            "The Reserve Bank of India Monetary Policy Committee kept benchmark repo rate unchanged at 6.50%, underscoring a cautious stance on food prices while raising GDP growth projections.",
            "The Monetary Policy Committee (MPC) of the Reserve Bank of India opted for status quo on the benchmark lending rate at 6.50% during its review. Sustained economic momentum across industrial production and services allows the central bank flexibility to prioritize lasting alignment of headline CPI with the 4% target. Persisting volatility in vegetable and cereal prices necessitates continued monetary vigilance. The committee reaffirmed its stance on withdrawal of accommodation, emphasizing that high real interest rates remain calibrated to dampen generalized price pressures without derailing capital expenditure cycles in the domestic economy. Structural reforms, digital public infrastructure, and fiscal prudence continue to attract record long-term capital inflows into the domestic economy.",
            "Economy",
            "Kavita Nair",
            "2026-09-05"
        ));

        ARTICLES.add(new NewsArticle(
            "art-108",
            "Digital Public Infrastructure and UPI Surge Propels Indian Fintech Innovation",
            "India Unified Payments Interface registers record transactions, driving automated reconciliation through graph clustering and string hashing.",
            "India's digital payment ecosystem achieved another record milestone with UPI logging billions of transactions monthly. Behind the scenes, payment routing architectures leverage distributed Rabin-Karp hashing algorithms and directed graph models to optimize interbank settlement paths and detect fraudulent payment loops in near real-time. Economists highlight that the widespread adoption of open digital protocols and account aggregator frameworks has democratized formal credit access for millions of micro-enterprises and rural businesses.",
            "Fintech",
            "Rohan Deshmukh",
            "2026-09-02"
        ));
    }

    public static List<NewsArticle> getAllArticles() {
        return Collections.unmodifiableList(ARTICLES);
    }

    public static NewsArticle getById(String id) {
        for (NewsArticle art : ARTICLES) {
            if (art.getId().equalsIgnoreCase(id)) {
                return art;
            }
        }
        return null;
    }

    public static int getCount() {
        return ARTICLES.size();
    }
}
