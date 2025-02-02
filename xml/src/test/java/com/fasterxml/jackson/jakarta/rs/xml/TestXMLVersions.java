package com.fasterxml.jackson.jakarta.rs.xml;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;

import static org.junit.jupiter.api.Assertions.*;

public class TestXMLVersions extends JakartaRSTestBase
{
    @Test
    public void testMapperVersions()
    {
        assertVersion(new JacksonXMLProvider());
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */
    
    private void assertVersion(Versioned vers)
    {
        final Version v = vers.version();
        assertFalse(v.isUnknownVersion(), "Should find version information (got "+v+")");
        Version exp = PackageVersion.VERSION;
        assertEquals(exp.toFullString(), v.toFullString());
        assertEquals(exp, v);
    }
}

