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
import com.jorgealfonsogarcia.recommender.domain.models.MovieResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.jorgealfonsogarcia.recommender.utils.ResilienceUtils.applyResilienceForFlux;
import static com.jorgealfonsogarcia.recommender.utils.ResilienceUtils.applyResilienceForMono;

/**
 * Service for the movie resource.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
@Service
public class MovieService {

    private final CacheManager caffeineCacheManager;
    private final WebClient movieServiceWebClient;
    private final CircuitBreaker moviesCircuitBreaker;
    private final Retry moviesRetry;

    /**
     * Constructor.
     *
     * @param caffeineCacheManager  The Caffeine cache manager.
     * @param movieServiceWebClient The movie service web client.
     * @param moviesCircuitBreaker  The movies circuit breaker.
     * @param moviesRetry           The movies retry.
     */
    @Autowired
    public MovieService(CacheManager caffeineCacheManager,
                        WebClient movieServiceWebClient,
                        CircuitBreaker moviesCircuitBreaker,
                        Retry moviesRetry) {
        this.caffeineCacheManager = caffeineCacheManager;
        this.movieServiceWebClient = movieServiceWebClient;
        this.moviesCircuitBreaker = moviesCircuitBreaker;
        this.moviesRetry = moviesRetry;
    }

    /**
     * Searches movies by year range, genres and language.
     *
     * @param startYear The start year.
     * @param endYear   The end year.
     * @param genres    The genres.
     * @param language  The language. It should be ISO 639-1.
     * @return A Flux with the movies found sorted by release date.
     */
    public Flux<MovieResponse> search(final Integer startYear,
                                      final Integer endYear,
                                      final List<String> genres,
                                      final String language) {
        final var genresMono = getGenres(language);
        return genresMono.flatMapMany(genreList -> {
                    final var joinedGenreIds = getGenreIdsJoined(genres, genreList);
                    return Flux.range(startYear, endYear - startYear + 1)
                            .flatMap(year -> getMovieFromApi(year, joinedGenreIds, language))
                            .map(movie -> getMovieResponseFunction(movie, genreList));
                })
                .sort(Comparator.comparing(movieResponse -> LocalDate.parse(movieResponse.releaseDate())));
    }

    /**
     * Gets all the genres by language.
     *
     * @param language The language. It should be ISO 639-1.
     * @return A Mono with the genres found.
     */
    public Mono<List<Genre>> getGenres(final String language) {
        var cacheKey = "genres_%s".formatted(language);
        var cache = caffeineCacheManager.getCache("moviesCache");

        if (cache == null) {
            return getGenresFromApi(language);
        }

        return Mono.justOrEmpty(cache.get(cacheKey))
                .mapNotNull(value -> {
                    var o = value.get();
                    if (o instanceof List) {
                        //noinspection unchecked
                        return (List<Genre>) o;
                    }

                    throw new ClassCastException("Unexpected type in cache: %s"
                            .formatted(o != null ? o.getClass().getName() : "null"));
                })
                .switchIfEmpty(Mono.defer(() -> getGenresFromApi(language))
                        .doOnSuccess(genres -> cache.put(cacheKey, genres)));
    }

    /**
     * Gets all the languages.
     *
     * @return A Flux with the languages found.
     */
    public Flux<Language> getLanguages() {
        return applyResilienceForFlux(() -> movieServiceWebClient.get()
                        .uri("/configuration/languages")
                        .retrieve()
                        .bodyToFlux(Language.class)
                        .sort(Comparator.comparing(Language::englishName)),
                moviesCircuitBreaker, moviesRetry);
    }

    private MovieResponse getMovieResponseFunction(final Movie movie,
                                                   final List<Genre> genreList) {
        final var genreNames = genreList.stream()
                .filter(genre -> movie.genreIds().contains(genre.id()))
                .map(Genre::name)
                .toList();

        return new MovieResponse(
                movie.id(),
                genreNames,
                movie.originalLanguage(),
                movie.originalTitle(),
                movie.title(),
                movie.overview(),
                movie.popularity().intValue(),
                movie.releaseDate()
        );
    }

    private String getGenreIdsJoined(final List<String> genreNames,
                                     final List<Genre> genreList) {
        return genreList.stream()
                .filter(genre -> genreNames.contains(genre.name()))
                .map(Genre::id)
                .toList().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private Flux<Movie> getMovieFromApi(final Integer primaryReleaseYear,
                                        final String genreIds,
                                        final String language) {
        return applyResilienceForFlux(() -> movieServiceWebClient.get()
                        .uri("/discover/movie?" +
                                        "include_adult=false&" +
                                        "include_video=false&" +
                                        "primary_release_year={primaryReleaseYear}&" +
                                        "with_genres={genre}&" +
                                        "with_original_language={language}&" +
                                        "sort_by=popularity.desc",
                                primaryReleaseYear,
                                genreIds,
                                language)
                        .retrieve()
                        .bodyToFlux(MoviePageResponse.class)
                        .map(MoviePageResponse::results)
                        .flatMap(Flux::fromIterable),
                moviesCircuitBreaker, moviesRetry);
    }

    private Mono<List<Genre>> getGenresFromApi(final String language) {
        return applyResilienceForMono(() -> movieServiceWebClient.get()
                        .uri("/genre/movie/list?language={language}", language)
                        .retrieve()
                        .bodyToFlux(GenresResponse.class)
                        .map(GenresResponse::genres)
                        .flatMap(Flux::fromIterable)
                        .sort(Comparator.comparing(Genre::id))
                        .collectList(),
                moviesCircuitBreaker, moviesRetry);
    }
}
