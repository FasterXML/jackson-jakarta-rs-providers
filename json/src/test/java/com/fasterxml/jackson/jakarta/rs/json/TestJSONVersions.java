package com.fasterxml.jackson.jakarta.rs.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;

public class TestJSONVersions extends JakartaRSTestBase
{
    public void testMapperVersions()
    {
        assertVersion(new JacksonJsonProvider());
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */
    
    private void assertVersion(Versioned vers)
    {
        final Version v = vers.version();
        assertFalse("Should find version information (got "+v+")", v.isUnknownVersion());
        Version exp = PackageVersion.VERSION;
        assertEquals(exp.toFullString(), v.toFullString());
        assertEquals(exp, v);
    }
}

