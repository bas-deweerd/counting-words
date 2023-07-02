package ordina;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class StringSplitterTest {

    StringSplitter splitter = new StringSplitter();

    @ParameterizedTest
    @ValueSource(strings = {"hello my friend", "hello 1my 22 8 friend", "hello, @my@ friend!"})
    void getWordsInLowerCase_withDifferentVariations_shouldSplitIntoWords(String text) {
        // When
        List<String> result = splitter.getWords(text);

        // Then
        assertThat(result.size(), is(3));
        assertThat(result, hasItems("hello", "my", "friend"));
    }

    @Test
    void getWordsInLowerCase_withDuplicates_shouldCountAsSeparateWords() {
        // Given
        String text = "hello hello my friend";

        // When
        List<String> result = splitter.getWords(text);

        // Then
        assertThat(result.size(), is(4));
        assertThat(result, hasItems("hello", "my", "friend"));
    }
}