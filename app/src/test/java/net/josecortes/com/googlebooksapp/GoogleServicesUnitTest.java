package net.josecortes.com.googlebooksapp;

import net.josecortes.com.googlebooksapp.externalservices.GoogleServices;

import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * The goal of this class is to perform some samples and basic unit tests for the sanity method in the class
 * GoogleServices. More tests must should be provided in a real environment.
 */

public class GoogleServicesUnitTest {
    @Test
    public void sanitizeNull_returnsNull() throws Exception {
        assertNull(GoogleServices.sanitizeQuery(null));
    }

    @Test
    public void sanitizeEmpty_returnsEmptyString() throws Exception {
        assertTrue("".equals(GoogleServices.sanitizeQuery("")));
    }

    @Test
    public void sanitizeBlanks_returnsEmptyString() throws Exception {
        assertTrue("".equals(GoogleServices.sanitizeQuery(" ")));
    }

    @Test
    public void sanitizeIdentity_returnsSameString() throws Exception {
        assertTrue("abcdef".equals(GoogleServices.sanitizeQuery("abcdef")));
    }

    @Test
    public void sanitizeAmps_returnsAmpsRemoved() throws Exception {
        assertTrue("abcd".equals(GoogleServices.sanitizeQuery("a&b&c&d&")));
    }

    @Test
    public void sanitizeSlash_returnsSlashRemoved() throws Exception {
        assertTrue("abcd".equals(GoogleServices.sanitizeQuery("/a/b/c/d/")));
    }

    @Test
    public void sanitizeRandomTokens_returnsTokensRemoved() throws Exception {
        assertTrue("abcd".equals(GoogleServices.sanitizeQuery("&&/a/b&&/&//c/d/&&&")));
    }
}