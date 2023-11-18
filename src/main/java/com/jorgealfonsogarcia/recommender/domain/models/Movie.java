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

package com.jorgealfonsogarcia.recommender.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a movie.
 *
 * @param adult            Whether the movie is for adults.
 * @param backdropPath     The backdrop path.
 * @param genreIds         The genre ids.
 * @param id               The id.
 * @param originalLanguage The original language. It is represented by a ISO 639-1 code.
 * @param originalTitle    The original title.
 * @param overview         The overview.
 * @param popularity       The popularity.
 * @param posterPath       The poster path.
 * @param releaseDate      The release date. It is represented by a ISO 8601 format.
 * @param title            The title in English.
 * @param video            Whether the movie has video.
 * @param voteAverage      The vote average.
 * @param voteCount        The vote count.
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Movie(
        @JsonProperty("adult")
        Boolean adult,

        @JsonProperty("backdrop_path")
        String backdropPath,

        @JsonProperty("genre_ids")
        List<Integer> genreIds,

        @JsonProperty("id")
        Integer id,

        @JsonProperty("original_language")
        String originalLanguage,

        @JsonProperty("original_title")
        String originalTitle,

        @JsonProperty("overview")
        String overview,

        @JsonProperty("popularity")
        Double popularity,

        @JsonProperty("poster_path")
        String posterPath,

        @JsonProperty("release_date")
        String releaseDate,

        @JsonProperty("title")
        String title,

        @JsonProperty("video")
        Boolean video,

        @JsonProperty("vote_average")
        Double voteAverage,

        @JsonProperty("vote_count")
        Integer voteCount
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -987312235095950089L;

    /**
     * Constructor.
     *
     * @param adult            Whether the movie is for adults.
     * @param backdropPath     The backdrop path.
     * @param genreIds         The genre ids.
     * @param id               The id.
     * @param originalLanguage The original language. It is represented by a ISO 639-1 code.
     * @param originalTitle    The original title.
     * @param overview         The overview.
     * @param popularity       The popularity.
     * @param posterPath       The poster path.
     * @param releaseDate      The release date. It is represented by a ISO 8601 format.
     * @param title            The title in English.
     * @param video            Whether the movie has video.
     * @param voteAverage      The vote average.
     * @param voteCount        The vote count.
     */
    public Movie(Boolean adult,
                 String backdropPath,
                 List<Integer> genreIds,
                 Integer id,
                 String originalLanguage,
                 String originalTitle,
                 String overview,
                 Double popularity,
                 String posterPath,
                 String releaseDate,
                 String title,
                 Boolean video,
                 Double voteAverage,
                 Integer voteCount) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds == null ? List.of() : List.copyOf(genreIds);
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    @JsonProperty("genre_ids")
    @Override
    public List<Integer> genreIds() {
        return genreIds == null ? List.of() : List.copyOf(genreIds);
    }
}
