/*
 * MIT License
 *
 * COPYRIGHT (c) 2023 Jorge Garcia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jorgealfonsogarcia.recommender.controllers;

import com.jorgealfonsogarcia.recommender.domain.models.Genre;
import com.jorgealfonsogarcia.recommender.domain.models.Language;
import com.jorgealfonsogarcia.recommender.domain.models.MovieResponse;
import com.jorgealfonsogarcia.recommender.services.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link MovieController} class.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    /**
     * GIVEN: A year range greater than 5.
     * WHEN: Search movies.
     * THEN: Return a flux error.
     */
    @Test
    void givenYearRangeGreaterThan5_whenSearch_thenReturnFluxError() {
        var result = movieController.search(1982, 1992, null, null);

        assertNotNull(result);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    /**
     * GIVEN: More than 3 genres.
     * WHEN: Search movies.
     * THEN: Return a flux error.
     */
    @Test
    void givenMoreThan3Genres_whenSearch_thenReturnFluxError() {
        final var genres = IntStream.rangeClosed(1, 5)
                .mapToObj("Genre %d"::formatted)
                .toList();

        final var result = movieController.search(1982, 1985, genres, null);

        assertNotNull(result);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    /**
     * GIVEN: Valid parameters.
     * WHEN: Search movies.
     * THEN: Return a flux.
     */
    @Test
    void givenValidParameters_whenSearch_thenReturnFlux() {
        final var movieResponse = new MovieResponse(
                1,
                List.of("Genre 1", "Genre 2"),
                "lang",
                "Original Title",
                "Title",
                "Overview",
                5,
                "1982-01-01"
        );
        doReturn(Flux.just(movieResponse)).when(movieService).search(anyInt(), anyInt(), anyList(), anyString());

        final var result = movieController.search(1982, 1985, List.of("Genre 1", "Genre 2"),
                "lang");

        assertNotNull(result);

        StepVerifier.create(result)
                .assertNext(response -> assertEquals(movieResponse, response))
                .verifyComplete();

        verify(movieService).search(anyInt(), anyInt(), anyList(), anyString());
    }

    /**
     * GIVEN: Valid language.
     * WHEN: Get genres.
     * THEN: Return a mono.
     */
    @Test
    void givenValidLanguage_whenGetGenres_thenReturnMono() {
        final var genres = IntStream.rangeClosed(1, 2)
                .mapToObj(value -> new Genre(value, "Genre %d".formatted(value)))
                .toList();
        doReturn(Mono.just(genres)).when(movieService).getGenres(anyString());

        var result = movieController.getGenres("lang");

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNext(genres)
                .verifyComplete();

        verify(movieService).getGenres(anyString());
    }

    /**
     * GIVEN: Call.
     * WHEN: Get languages.
     * THEN: Return a flux.
     */
    @Test
    void givenCall_whenGetLanguages_thenReturnFlux() {
        final var language = new Language("en", "English", "English");
        final var languageFlux = Flux.just(language);
        doReturn(languageFlux).when(movieService).getLanguages();

        final var result = movieController.getLanguages();

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNext(language)
                .verifyComplete();

        verify(movieService).getLanguages();
    }
}