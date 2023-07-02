package ordina;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import ordina.core.FrequencyCalculator;
import ordina.models.WordFrequencyRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
class WordFrequencyAnalyzerTest {
    @Inject
    WordFrequencyAnalyzer analyser;

    @InjectMock
    FrequencyCalculator mockCalculator;

    @BeforeEach
    public void init() {
        when(mockCalculator.getFrequencies("Hello hello my friend"))
                .thenReturn(List.of(
                        new WordFrequencyRecord("hello", 2),
                        new WordFrequencyRecord("my", 1),
                        new WordFrequencyRecord("friend", 1)
                ));

        when(mockCalculator.getFrequencies("Hello my friend"))
                .thenReturn(List.of(
                        new WordFrequencyRecord("hello", 1),
                        new WordFrequencyRecord("my", 1),
                        new WordFrequencyRecord("friend", 1)
                ));

        when(mockCalculator.getFrequencies("The sun shines over the lake"))
                .thenReturn(List.of(
                        new WordFrequencyRecord("the", 2),
                        new WordFrequencyRecord("sun", 1),
                        new WordFrequencyRecord("shines", 1),
                        new WordFrequencyRecord("over", 1),
                        new WordFrequencyRecord("lake", 1)
                ));
    }

    @ParameterizedTest
    @CsvSource({"Hello my friend,1", "Hello hello my friend,2", ",0"})
    void calculateHighestFrequency_withDifferentVariations_shouldCountCorrectly(String text, int expectedFrequency) {
        // When
        int highestFrequency = analyser.calculateHighestFrequency(text);

        // Then
        assertThat(highestFrequency, is(expectedFrequency));
    }

    @ParameterizedTest
    @CsvSource({"hello,2", "HELLO,2", "my,1", "friend,1", "wrong,0"})
    void calculateFrequencyForWord_withDifferentVariations_shouldCountCorrectly(String word, int expectedFrequency) {
        // Given
        String text = "Hello hello my friend";

        // When
        int frequency = analyser.calculateFrequencyForWord(text, word);

        // Then
        assertThat(frequency, is(expectedFrequency));
    }

    @Test
    void calculateMostFrequentNWords_nIsThree_shouldReturnThreeRecordsInOrder() {
        // Given
        String text = "The sun shines over the lake";

        // When
        var result = analyser.calculateMostFrequentNWords(text, 3);

        // Then
        assertThat(result.size(), is(3));

        assertThat(result.get(0).word(), is("the"));
        assertThat(result.get(0).frequency(), is(2));

        assertThat(result.get(1).word(), is("lake"));
        assertThat(result.get(1).frequency(), is(1));

        assertThat(result.get(2).word(), is("over"));
        assertThat(result.get(2).frequency(), is(1));
    }

    @Test
    void calculateMostFrequentNWords_nIsTwo_shouldReturnTwoRecordsInOrder() {
        // Given
        String text = "The sun shines over the lake";

        // When
        var result = analyser.calculateMostFrequentNWords(text, 2);

        // Then
        assertThat(result.size(), is(2));

        assertThat(result.get(0).word(), is("the"));
        assertThat(result.get(0).frequency(), is(2));

        assertThat(result.get(1).word(), is("lake"));
        assertThat(result.get(1).frequency(), is(1));
    }

    @Test
    void calculateMostFrequentNWords_emptyText_shouldReturnZeroRecords() {
        // Given
        String text = "";

        // When
        var result = analyser.calculateMostFrequentNWords(text, 3);

        // Then
        assertThat(result.size(), is(0));
    }

}