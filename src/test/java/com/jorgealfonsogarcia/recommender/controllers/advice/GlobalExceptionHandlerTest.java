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

package com.jorgealfonsogarcia.recommender.controllers.advice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link GlobalExceptionHandler} class.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 17
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * GIVEN: An IllegalArgumentException.
     * WHEN:  The exception is handled.
     * THEN:  A ResponseEntity with the error response and an HTTP 400 status code is returned.
     */
    @Test
    void givenIllegalArgumentException_whenHandleIllegalArgumentException_thenReturnsErrorResponse() {
        final var message = "Test";
        final var exception = new IllegalArgumentException(message);

        final var result = globalExceptionHandler.handleIllegalArgumentException(exception);

        var body = result.getBody();
        assertNotNull(body);
        assertEquals("Invalid Request", body.error());
        assertEquals(message, body.message());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    /**
     * GIVEN: A WebClientResponseException.
     * WHEN:  The exception is handled.
     * THEN:  A ResponseEntity with the error response and an HTTP status code is returned.
     */
    @Test
    void givenWebClientResponseException_whenHandleWebClientResponseException_thenReturnsErrorResponse() {
        final var statusText = "Not Found";
        final var message = "Not Found parameter";
        final var httpStatus = HttpStatus.NOT_FOUND;

        final var exception = mock(WebClientResponseException.class);

        doReturn(httpStatus).when(exception).getStatusCode();
        doReturn(statusText).when(exception).getStatusText();
        doReturn(message).when(exception).getMessage();

        final var result = globalExceptionHandler.handleWebClientResponseException(exception);

        var body = result.getBody();
        assertNotNull(body);
        assertEquals(statusText, body.error());
        assertEquals(message, body.message());
        assertEquals(httpStatus, result.getStatusCode());

        verify(exception).getStatusCode();
        verify(exception).getStatusText();
        verify(exception).getMessage();
    }
}