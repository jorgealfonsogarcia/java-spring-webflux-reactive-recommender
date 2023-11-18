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
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Utility class for resilience.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
public final class ResilienceUtils {

    private ResilienceUtils() {
    }

    /**
     * Applies resilience to a {@link Mono} publisher.
     *
     * @param monoSupplier   The {@link Mono} supplier.
     * @param circuitBreaker The circuit breaker.
     * @param retry          The retry.
     * @param <T>            The type of the {@link Mono}.
     * @return The {@link Mono} publisher with resilience.
     */
    public static <T> Mono<T> applyResilienceForMono(final Supplier<Mono<T>> monoSupplier,
                                                     final CircuitBreaker circuitBreaker,
                                                     final Retry retry) {
        return Mono.defer(monoSupplier)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry));
    }

    /**
     * Applies resilience to a {@link Flux} publisher.
     *
     * @param monoSupplier   The {@link Flux} supplier.
     * @param circuitBreaker The circuit breaker.
     * @param retry          The retry.
     * @param <T>            The type of the {@link Flux}.
     * @return The {@link Flux} publisher with resilience.
     */
    public static <T> Flux<T> applyResilienceForFlux(final Supplier<Flux<T>> monoSupplier,
                                                     final CircuitBreaker circuitBreaker,
                                                     final Retry retry) {
        return Flux.defer(monoSupplier)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry));
    }
}
