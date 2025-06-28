package com.jiangxue.framework.common.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.List;

/**
 * Inserts headers to prevent caching if no cache control headers have been specified.
 * Specifically it adds the following headers:
 * <ul>
 * <li>Cache-Control: no-cache, no-store, max-age=0, must-revalidate</li>
 * <li>Pragma: no-cache</li>
 * <li>Expires: 0</li>
 * </ul>
 *
 */
public final class CacheControlHeadersWriter implements HeaderWriter {

    private static final String EXPIRES = "Expires";

    private static final String PRAGMA = "Pragma";

    private static final String CACHE_CONTROL = "Cache-Control";

    private final HeaderWriter delegate;

    /**
     * Creates a new instance
     */
    public CacheControlHeadersWriter() {
        this.delegate = new StaticHeadersWriter(createHeaders());
    }

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        if (hasHeader(response, CACHE_CONTROL) || hasHeader(response, EXPIRES) || hasHeader(response, PRAGMA)
                || response.getStatus() == HttpStatus.NOT_MODIFIED.value()) {
            return;
        }
        this.delegate.writeHeaders(request, response);
    }

    private boolean hasHeader(HttpServletResponse response, String headerName) {
        return response.getHeader(headerName) != null;
    }

    private static List<Header> createHeaders() {
        List<Header> headers = new ArrayList<>(3);
        headers.add(new Header(CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate"));
        headers.add(new Header(PRAGMA, "no-cache"));
        headers.add(new Header(EXPIRES, "0"));
        return headers;
    }

}
