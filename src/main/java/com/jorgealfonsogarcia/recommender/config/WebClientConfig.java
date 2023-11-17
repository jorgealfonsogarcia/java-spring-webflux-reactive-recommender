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

import com.jorgealfonsogarcia.recommender.controllers.component.XRequestIdExchangeFilterFunction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Configures the web client.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates the exchange filter function for the X-Request-Id header.
     *
     * @return The exchange filter function for the X-Request-Id header.
     */
    @Bean
    public ExchangeFilterFunction xRequestIdExchangeFilterFunction() {
        return new XRequestIdExchangeFilterFunction();
    }

    /**
     * Creates the web client for the movie service.
     *
     * @param movieServiceUrl                  The movie service URL.
     * @param authToken                        The auth token.
     * @param builder                          The web client builder.
     * @param xRequestIdExchangeFilterFunction The exchange filter function for the X-Request-Id header.
     * @return The web client for the movie service.
     */
    @Bean
    public WebClient movieServiceWebClient(@Value("${movie.service.url}") String movieServiceUrl,
                                           @Value("${AUTH_TOKEN}") String authToken,
                                           WebClient.Builder builder,
                                           ExchangeFilterFunction xRequestIdExchangeFilterFunction) {
        return builder
                .baseUrl(movieServiceUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setContentType(APPLICATION_JSON);
                    httpHeaders.setBearerAuth(authToken);
                })
                .filter(xRequestIdExchangeFilterFunction)
                .build();
    }
}
