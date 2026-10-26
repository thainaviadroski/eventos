package net.eventos_facu.eventos.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class FullTextUtils {


    private static final int MIN_TOKEN_SIZE = 2;

    private static final Set<String> STOPWORDS = Set.of(
            "a", "about", "an", "are", "as", "at", "be", "by", "com", "de", "en", "for", "from",
            "how", "i", "in", "is", "it", "la", "of", "on", "or", "that", "the", "this", "to",
            "was", "what", "when", "where", "who", "will", "with", "und", "www"
    );

    private FullTextUtils() {
    }


    public static String toBooleanQuery(String termo) {
        if (termo == null) {
            return "";
        }


        String limpo = termo.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}_]+", " ").trim();

        return Arrays.stream(limpo.split(" "))
                .filter(palavra -> palavra.length() >= MIN_TOKEN_SIZE)
                .filter(palavra -> !STOPWORDS.contains(palavra))
                .distinct()
                .map(palavra -> "+" + palavra + "*")
                .collect(Collectors.joining(" "));
    }
}
