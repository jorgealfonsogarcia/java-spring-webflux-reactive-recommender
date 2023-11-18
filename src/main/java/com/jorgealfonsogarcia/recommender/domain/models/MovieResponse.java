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

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a movie's response.
 *
 * @param id               The id.
 * @param genres           The genres name.
 * @param originalLanguage The original language.
 * @param originalTitle    The original title.
 * @param title            The title in English.
 * @param overview         The overview.
 * @param popularity       The popularity.
 * @param releaseDate      The release date. It is represented by a ISO 8601 format.
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
public record MovieResponse(
        Integer id,
        List<String> genres,
        String originalLanguage,
        String originalTitle,
        String title,
        String overview,
        Integer popularity,
        String releaseDate
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 8932131531162635163L;

    /**
     * Constructor.
     *
     * @param id               The id.
     * @param genres           The genres name.
     * @param originalLanguage The original language.
     * @param originalTitle    The original title.
     * @param title            The title in English.
     * @param overview         The overview.
     * @param popularity       The popularity.
     * @param releaseDate      The release date. It is represented by a ISO 8601 format.
     */
    public MovieResponse(Integer id,
                         List<String> genres,
                         String originalLanguage,
                         String originalTitle,
                         String title,
                         String overview,
                         Integer popularity,
                         String releaseDate) {
        this.id = id;
        this.genres = genres == null ? List.of() : List.copyOf(genres);
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
    }

    @Override
    public List<String> genres() {
        return genres == null ? List.of() : List.copyOf(genres);
    }
}
