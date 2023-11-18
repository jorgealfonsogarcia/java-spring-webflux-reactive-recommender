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

package com.jorgealfonsogarcia.recommender.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.jorgealfonsogarcia.recommender.utils.ResilienceUtils.applyResilienceForFlux;
import static com.jorgealfonsogarcia.recommender.utils.ResilienceUtils.applyResilienceForMono;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link ResilienceUtils}.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
class ResilienceUtilsTest {

    private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("circuitBreaker");
    private final Retry retry = Retry.ofDefaults("retry");

    /**
     * GIVEN: A {@link Mono} supplier.
     * WHEN: Apply resilience to the {@link Mono} supplier.
     * THEN: The {@link Mono} supplier with resilience is returned.
     */
    @Test
    void givenMonoSupplier_whenApplyResilienceForMono_thenMonoWithResilience() {
        final var result = applyResilienceForMono(() -> Mono.just("OK"), circuitBreaker, retry);

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNext("OK")
                .verifyComplete();
    }

    /**
     * GIVEN: A {@link Flux} supplier.
     * WHEN: Apply resilience to the {@link Flux} supplier.
     * THEN: The {@link Flux} supplier with resilience is returned.
     */
    @Test
    void givenFluxSupplier_whenApplyResilienceForFlux_thenFluxWithResilience() {
        final var result = applyResilienceForFlux(() -> Flux.just("OK-1", "OK-2"), circuitBreaker, retry);

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNext("OK-1", "OK-2")
                .verifyComplete();
    }
}