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

package com.jorgealfonsogarcia.recommender.monitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.actuate.health.Status.UP;

/**
 * Unit tests for {@link AppVersionHealthIndicator} class.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
class AppVersionHealthIndicatorTest {

    private static final String VERSION = "1.0.0";

    private final AppVersionHealthIndicator appVersionHealthIndicator = new AppVersionHealthIndicator(VERSION);

    /**
     * GIVEN:   A call to the health indicator.
     * WHEN:    The health indicator is called.
     * THEN:    The status is up and the version is returned.
     */
    @Test
    void givenCall_whenHealth_thenStatusUpAndVersion() {
        final var result = appVersionHealthIndicator.health();

        assertEquals(UP, result.getStatus());
        assertEquals(VERSION, result.getDetails().get("version"));
    }
}