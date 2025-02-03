package tools.jackson.jakarta.rs.smile;

import tools.jackson.core.Version;
import tools.jackson.core.Versioned;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSmileVersions extends JakartaRSTestBase
{
    @Test
    public void testMapperVersions()
    {
        assertVersion(new JacksonSmileProvider());
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

