package ordina;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ordina.models.WordFrequency;
import ordina.models.WordFrequencyRecord;

import java.util.List;

import static java.lang.Math.toIntExact;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.*;

@ApplicationScoped
public class FrequencyCalculator {
    @Inject
    StringSplitter stringSplitter;

    public List<WordFrequency> getFrequencies(String text) {
        return stringSplitter
                .getWords(text.toLowerCase())
                .stream()
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .map(entry -> new WordFrequencyRecord(entry.getKey(), toIntExact(entry.getValue())))
                .collect(toUnmodifiableList());
    }
}
