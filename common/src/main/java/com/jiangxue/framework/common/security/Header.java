package com.jiangxue.framework.common.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Header to be added to the {@link HttpServletResponse}
 */
public final class Header {

    private final String headerName;

    private final List<String> headerValues;

    /**
     * Creates a new instance
     * @param headerName the name of the header
     * @param headerValues the values of the header
     */
    public Header(String headerName, String... headerValues) {
        Assert.hasText(headerName, "headerName is required");
        Assert.notEmpty(headerValues, "headerValues cannot be null or empty");
        Assert.noNullElements(headerValues, "headerValues cannot contain null values");
        this.headerName = headerName;
        this.headerValues = Arrays.asList(headerValues);
    }

    /**
     * Gets the name of the header. Cannot be <code>null</code>.
     * @return the name of the header.
     */
    public String getName() {
        return this.headerName;
    }

    /**
     * Gets the values of the header. Cannot be null, empty, or contain null values.
     * @return the values of the header
     */
    public List<String> getValues() {
        return this.headerValues;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Header other = (Header) obj;
        if (!this.headerName.equals(other.headerName)) {
            return false;
        }
        return this.headerValues.equals(other.headerValues);
    }

    @Override
    public int hashCode() {
        return this.headerName.hashCode() + this.headerValues.hashCode();
    }

    @Override
    public String toString() {
        return "Header [name: " + this.headerName + ", values: " + this.headerValues + "]";
    }

}
