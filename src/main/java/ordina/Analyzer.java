package ordina;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ordina.core.FrequencyCalculator;
import ordina.models.WordFrequency;

import java.util.List;

import static java.util.Comparator.comparing;

@ApplicationScoped
public class Analyzer implements WordFrequencyAnalyzer {
    @Inject
    FrequencyCalculator frequencyCalculator;

    @Override
    public int calculateHighestFrequency(String text) {
        return frequencyCalculator.getFrequencies(text)
                .stream()
                .max(comparing(WordFrequency::frequency))
                .map(WordFrequency::frequency)
                .orElse(0);
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        return frequencyCalculator.getFrequencies(text)
                .stream()
                .filter(wordFrequency -> wordFrequency.word().equalsIgnoreCase(word))
                .findAny()
                .map(WordFrequency::frequency)
                .orElse(0);
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        return frequencyCalculator.getFrequencies(text)
                .stream()
                .sorted(comparing(WordFrequency::frequency).reversed()
                        .thenComparing(WordFrequency::word))
                .limit(n)
                .toList();
    }
}
