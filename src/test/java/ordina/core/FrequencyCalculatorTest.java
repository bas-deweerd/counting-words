package ordina.core;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import ordina.models.WordFrequency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class FrequencyCalculatorTest {

    @InjectMock
    StringSplitter mockStringSplitter;

    @Inject
    FrequencyCalculator calculator;

    boolean hasWord(List<WordFrequency> frequencies, String word, int numberOfOccurrences) {
        return frequencies.stream()
                .anyMatch(record ->
                        record.word().equals(word) && record.frequency() == numberOfOccurrences
                );
    }

    @Test
    void getFrequencies_wordsOccurOnce_shouldCountOncePerWord() {
        // Given
        when(mockStringSplitter.getWords("hello my friend")).thenReturn(List.of("hello", "my", "friend"));
        String text = "hello my friend";

        // When
        List<WordFrequency> frequencies = calculator.getFrequencies(text);

        // Then
        assertThat(frequencies.size(), is(3));
        assertTrue(hasWord(frequencies, "hello", 1));
        assertTrue(hasWord(frequencies, "my", 1));
        assertTrue(hasWord(frequencies, "friend", 1));
    }

    @Test
    void getFrequencies_wordsOccurMultipleTimes_shouldCountMultipleTimes() {
        // Given
        when(mockStringSplitter.getWords("hello hello hello my my friend"))
                .thenReturn(List.of("hello", "hello", "hello", "my", "my", "friend"));
        String text = "hello hello hello my my friend";

        // When
        List<WordFrequency> frequencies = calculator.getFrequencies(text);

        // Then
        assertThat(frequencies.size(), is(3));
        assertTrue(hasWord(frequencies, "hello", 3));
        assertTrue(hasWord(frequencies, "my", 2));
        assertTrue(hasWord(frequencies, "friend", 1));
    }

    @Test
    void getFrequencies_withCapitalAndLowerCase_shouldCountMultipleTimes() {
        // Given
        when(mockStringSplitter.getWords("hello hello hello my my friend"))
                .thenReturn(List.of("hello", "hello", "hello", "my", "my", "friend"));
        String text = "Hello hELLO hello my mY friend";

        // When
        List<WordFrequency> frequencies = calculator.getFrequencies(text);

        // Then
        assertThat(frequencies.size(), is(3));
        assertTrue(hasWord(frequencies, "hello", 3));
        assertTrue(hasWord(frequencies, "my", 2));
        assertTrue(hasWord(frequencies, "friend", 1));
    }
}