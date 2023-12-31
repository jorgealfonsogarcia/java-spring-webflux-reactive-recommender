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
 * Represents a movie's genres response.
 *
 * @param genres The genres.
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenresResponse(
        @JsonProperty("genres")
        List<Genre> genres
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6909083000233693475L;

    /**
     * Constructor.
     *
     * @param genres the genres.
     */
    public GenresResponse(List<Genre> genres) {
        this.genres = genres == null ? List.of() : List.copyOf(genres);
    }

    @JsonProperty("genres")
    @Override
    public List<Genre> genres() {
        return genres == null ? List.of() : List.copyOf(genres);
    }
}
