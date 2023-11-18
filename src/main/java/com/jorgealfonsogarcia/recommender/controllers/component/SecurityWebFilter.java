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

package com.jorgealfonsogarcia.recommender.controllers.component;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.jorgealfonsogarcia.recommender.config.ApplicationHttpHeaders.CONTENT_SECURITY_POLICY;
import static com.jorgealfonsogarcia.recommender.config.ApplicationHttpHeaders.STRICT_TRANSPORT_SECURITY;
import static com.jorgealfonsogarcia.recommender.config.ApplicationHttpHeaders.X_CONTENT_TYPE_OPTIONS;
import static com.jorgealfonsogarcia.recommender.config.ApplicationHttpHeaders.X_FRAME_OPTIONS;
import static com.jorgealfonsogarcia.recommender.config.ApplicationHttpHeaders.X_XSS_PROTECTION;

/**
 * Adds security headers to the response.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
@Component
@Order(-1)
public class SecurityWebFilter implements WebFilter {

    @SuppressWarnings({"NullableProblems", "SpellCheckingInspection"})
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final var headers = exchange.getResponse().getHeaders();
        headers.set(X_CONTENT_TYPE_OPTIONS.getHeader(), "nosniff");
        headers.set(CONTENT_SECURITY_POLICY.getHeader(), "default-src 'self'; object-src 'none';");
        headers.set(X_FRAME_OPTIONS.getHeader(), "DENY");
        headers.set(X_XSS_PROTECTION.getHeader(), "1; mode=block");
        headers.set(STRICT_TRANSPORT_SECURITY.getHeader(), "max-age=31536000; includeSubDomains");
        return chain.filter(exchange);
    }
}
