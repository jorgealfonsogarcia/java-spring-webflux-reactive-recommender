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

package com.jorgealfonsogarcia.recommender.services;

import com.jorgealfonsogarcia.recommender.domain.models.Genre;
import com.jorgealfonsogarcia.recommender.domain.models.GenresResponse;
import com.jorgealfonsogarcia.recommender.domain.models.Language;
import com.jorgealfonsogarcia.recommender.domain.models.Movie;
import com.jorgealfonsogarcia.recommender.domain.models.MoviePageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link MovieService} class.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private CacheManager caffeineCacheManager;

    @Mock
    private WebClient movieServiceWebClient;

    @InjectMocks
    private MovieService movieService;

    /**
     * GIVEN: Valid parameters.
     * WHEN: Search movies.
     * THEN: Return a flux of movie response.
     */
    @Test
    void givenValidParameters_whenSearch_thenReturnFluxMovieResponse() {
        doReturn(null).when(caffeineCacheManager).getCache(anyString());

        final var uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        doReturn(uriSpec).when(movieServiceWebClient).get();

        final var headersSpec = mock(WebClient.RequestHeadersSpec.class);
        doReturn(headersSpec).when(uriSpec).uri(anyString(), any(Object[].class));

        final var responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(responseSpec).when(headersSpec).retrieve();

        final var genresResponseFlux = Flux.just(
                new GenresResponse(List.of(new Genre(1, "Genre 1"), new Genre(2, "Genre 2")))
        );
        doReturn(genresResponseFlux).when(responseSpec).bodyToFlux(GenresResponse.class);

        final var movies = List.of(new Movie(
                false,
                "backdrop/path",
                List.of(1, 2),
                1,
                "lang",
                "Original Title",
                "Overview",
                1.0,
                "poster/path",
                "1982-01-01",
                "Title",
                false,
                5.0,
                10
        ));
        final var moviePageResponseFlux = Flux.just(
                new MoviePageResponse(1, movies, 1, 1)
        );
        doReturn(moviePageResponseFlux).when(responseSpec).bodyToFlux(MoviePageResponse.class);

        final var result = movieService.search(1982, 1985, List.of("Genre 1", "Genre 2"),
                "lang");

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNextCount(4)
                .verifyComplete();

        verify(caffeineCacheManager).getCache(anyString());
        verify(movieServiceWebClient, times(5)).get();
        verify(uriSpec, times(5)).uri(anyString(), any(Object[].class));
        verify(headersSpec, times(5)).retrieve();
        verify(responseSpec).bodyToFlux(GenresResponse.class);
        verify(responseSpec, times(4)).bodyToFlux(MoviePageResponse.class);
    }

    @Test
    void givenValidParametersWithCache_whenGetGenres_thenReturnMonoGenres() {
        final var cache = mock(Cache.class);
        doReturn(cache).when(caffeineCacheManager).getCache(anyString());

        final var valueWrapper = mock(Cache.ValueWrapper.class);
        doReturn(valueWrapper).when(cache).get(anyString());

        final var genres = List.of(new Genre(1, "Genre 1"), new Genre(2, "Genre 2"));
        doReturn(genres).when(valueWrapper).get();

        final var result = movieService.getGenres("lang");

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNext(genres)
                .verifyComplete();

        verify(caffeineCacheManager).getCache(anyString());
        verify(cache).get(anyString());
        verify(valueWrapper).get();
    }

    private static Stream<Arguments> givenValidParametersWithCacheWithWrongValues_whenGetGenres_thenReturnMonoClassCastException_source() {
        return Stream.of(
                Arguments.of("No Expected Value"),
                Arguments.of((Object) null)
        );
    }

    /**
     * GIVEN: Valid parameters with cache with wrong values.
     * WHEN: Get genres.
     * THEN: Return a mono with class cast exception.
     *
     * @param value The wrong value.
     */
    @ParameterizedTest
    @MethodSource("givenValidParametersWithCacheWithWrongValues_whenGetGenres_thenReturnMonoClassCastException_source")
    void givenValidParametersWithCacheWithWrongValues_whenGetGenres_thenReturnMonoClassCastException(final Object value) {
        final var cache = mock(Cache.class);
        doReturn(cache).when(caffeineCacheManager).getCache(anyString());

        final var valueWrapper = mock(Cache.ValueWrapper.class);
        doReturn(valueWrapper).when(cache).get(anyString());

        doReturn(value).when(valueWrapper).get();

        final var result = movieService.getGenres("lang");

        assertNotNull(result);

        StepVerifier.create(result)
                .expectError(ClassCastException.class)
                .verify();

        verify(caffeineCacheManager).getCache(anyString());
        verify(cache).get(anyString());
        verify(valueWrapper).get();
    }

    /**
     * GIVEN: Call.
     * WHEN: Get languages.
     * THEN: Return a flux of languages sorted by English name.
     */
    @Test
    void givenCall_whenGetLanguages_thenReturnFluxLanguagesSortedByEnglishName() {
        final var uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        doReturn(uriSpec).when(movieServiceWebClient).get();

        final var headersSpec = mock(WebClient.RequestHeadersSpec.class);
        doReturn(headersSpec).when(uriSpec).uri(anyString());

        final var responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(responseSpec).when(headersSpec).retrieve();

        //noinspection SpellCheckingInspection
        final var esLanguage = new Language("es", "Spanish", "Español");
        final var enLanguage = new Language("en", "English", "English");
        final var frLanguage = new Language("fr", "French", "Français");
        final var itLanguage = new Language("it", "Italian", "Italiano");
        final var deLanguage = new Language("de", "German", "Deutsch");

        final var languageFlux = Flux.just(esLanguage, enLanguage, frLanguage, itLanguage, deLanguage);
        doReturn(languageFlux).when(responseSpec).bodyToFlux(Language.class);

        final var result = movieService.getLanguages();

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNext(enLanguage, frLanguage, deLanguage, itLanguage, esLanguage)
                .verifyComplete();

        verify(movieServiceWebClient).get();
        verify(uriSpec).uri(anyString());
        verify(headersSpec).retrieve();
        verify(responseSpec).bodyToFlux(Language.class);
    }
}