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

/**
 * Represents the application HTTP headers.
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 21
 */
public enum ApplicationHttpHeaders {
    /**
     * The Content-Security-Policy header.
     */
    CONTENT_SECURITY_POLICY("Content-Security-Policy"),

    /**
     * The Strict-Transport-Security header.
     */
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),

    /**
     * The X-Content-Type-Options header.
     */
    X_CONTENT_TYPE_OPTIONS("X-Content-Type-Options"),

    /**
     * The X-Frame-Options header.
     */
    X_FRAME_OPTIONS("X-Frame-Options"),

    /**
     * The X-Request-Id header.
     */
    X_REQUEST_ID("X-Request-Id"),

    /**
     * The X-XSS-Protection header.
     */
    X_XSS_PROTECTION("X-XSS-Protection");

    private final String header;

    /**
     * Constructor.
     *
     * @param header The header.
     */
    ApplicationHttpHeaders(String header) {
        this.header = header;
    }

    /**
     * Gets the header.
     *
     * @return The header.
     */
    public String getHeader() {
        return header;
    }
}
