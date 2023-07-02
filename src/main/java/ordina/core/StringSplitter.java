package ordina.core;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class StringSplitter {
    static final String WORDS_REGEX = "\\P{L}+";

    public List<String> getWords(String text) {
        return Arrays.stream(text.split(WORDS_REGEX)).toList();
    }
}
