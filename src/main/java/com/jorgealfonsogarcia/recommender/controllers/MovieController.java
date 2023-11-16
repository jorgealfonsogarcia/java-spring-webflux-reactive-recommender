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
import com.jorgealfonsogarcia.recommender.domain.models.MovieResponse;
import com.jorgealfonsogarcia.recommender.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST controller for the movie resource.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
@Tag(name = "Movie Controller", description = "API endpoints related to movie operations")
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    /**
     * Constructor.
     *
     * @param movieService The movie service.
     */
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Searches movies by year range, genres and language.
     *
     * @param startYear The start year.
     * @param endYear   The end year. It should not exceed 5 years from the start year.
     * @param genres    The genres. It should not exceed 3 genres.
     * @param language  The language. It should be ISO 639-1.
     * @return A Flux with the movies found sorted by release date.
     */
    @Operation(summary = "Search movies",
            description = "Search for movies by a range of years, list of genres, and language.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of movie list",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping("/search")
    public Flux<MovieResponse> search(
            @Parameter(description = "The start year")
            @RequestParam Integer startYear,

            @Parameter(description = "The end year. It should not exceed 5 years from the start year")
            @RequestParam Integer endYear,

            @Parameter(description = "The genres. It should not exceed 3 genres")
            @RequestParam List<String> genres,

            @Parameter(description = "The language. It should be ISO 639-1")
            @RequestParam String language
    ) {

        if (endYear - startYear > 5) {
            return Flux.error(new IllegalArgumentException("Year range should not exceed 5 years"));
        }

        if (genres.size() > 3) {
            return Flux.error(new IllegalArgumentException("Genres should not exceed 3"));
        }

        return movieService.search(startYear, endYear, genres, language);
    }

    /**
     * Gets all the genres.
     *
     * @param language The language. It should be ISO 639-1.
     * @return A Mono with the genres found.
     */
    @Operation(summary = "Get all genres",
            description = "Retrieves a list of all genres for a specified language.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of genres",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Genres not found for the specified language"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping("/genres/{language}")
    public Mono<List<Genre>> getGenres(
            @Parameter(description = "The ISO 639-1 language code")
            @PathVariable String language
    ) {
        return movieService.getGenres(language);
    }
}
