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

package com.jorgealfonsogarcia.recommender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Objects;

/**
 * Configures the application properties.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final String version;
    private final String artifactId;

    /**
     * Constructor.
     *
     * @param version    The application version.
     * @param artifactId The application artifact id.
     */
    @ConstructorBinding
    public AppProperties(String version, String artifactId) {
        this.version = version;
        this.artifactId = artifactId;
    }

    /**
     * Gets the application version.
     *
     * @return The application version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the application artifact id.
     *
     * @return The application artifact id.
     */
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final var that = (AppProperties) o;
        return Objects.equals(version, that.version) && Objects.equals(artifactId, that.artifactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, artifactId);
    }

    @Override
    public String toString() {
        return "AppProperties{version='%s', artifactId='%s'}".formatted(version, artifactId);
    }
}
