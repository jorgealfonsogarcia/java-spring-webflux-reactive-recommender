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
 * Represents a movie's page response.
 *
 * @param page         The page number.
 * @param results      The movies.
 * @param totalPages   The total number of pages.
 * @param totalResults The total number of results.
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MoviePageResponse(
        @JsonProperty("page")
        Integer page,

        @JsonProperty("results")
        List<Movie> results,

        @JsonProperty("total_pages")
        Integer totalPages,

        @JsonProperty("total_results")
        Integer totalResults
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 5056144134119963251L;

    /**
     * Constructor.
     *
     * @param page         The page number.
     * @param results      The movies.
     * @param totalPages   The total number of pages.
     * @param totalResults The total number of results.
     */
    public MoviePageResponse(Integer page,
                             List<Movie> results,
                             Integer totalPages,
                             Integer totalResults) {
        this.page = page;
        this.results = results == null ? List.of() : List.copyOf(results);
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    @Override
    public List<Movie> results() {
        return results == null ? List.of() : List.copyOf(results);
    }
}
