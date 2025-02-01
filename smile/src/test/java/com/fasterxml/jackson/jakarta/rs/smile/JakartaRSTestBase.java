package com.fasterxml.jackson.jakarta.rs.smile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public abstract class JakartaRSTestBase
{
    /*
    /**********************************************************
    /* Additional assertion methods
    /**********************************************************
     */

    protected void assertType(Object ob, Class<?> expType)
    {
        if (ob == null) {
            fail("Expected an object of type "+expType.getName()+", got null");
        }
        Class<?> cls = ob.getClass();
        if (!expType.isAssignableFrom(cls)) {
            fail("Expected type "+expType.getName()+", got "+cls.getName());
        }
    }

    protected void verifyException(Throwable e, String... matches)
    {
        String msg = e.getMessage();
        String lmsg = (msg == null) ? "" : msg.toLowerCase();
        for (String match : matches) {
            String lmatch = match.toLowerCase();
            if (lmsg.indexOf(lmatch) >= 0) {
                return;
            }
        }
        fail("Expected an exception with one of substrings ("+Arrays.asList(matches)+"): got one with message \""+msg+"\"");
    }
    
    protected void _verifyBytes(byte[] actBytes, byte... expBytes)
    {
        assertArrayEquals(expBytes, actBytes);
    }

    /*
    /**********************************************************
    /* Other helper methods
    /**********************************************************
     */

    public String q(String str) {
        return '"'+str+'"';
    }

    protected String a2q(String json) {
        return json.replace("'", "\"");
    }

    protected String readUTF8(InputStream in) throws IOException {
        return readUTF8(in, Integer.MAX_VALUE);
    }

    protected String readUTF8(InputStream in, int maxLen) throws IOException {
        return asUTF8(readAll(in), maxLen);
    }

    protected byte[] readAll(InputStream in) throws IOException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(100);
        byte[] buffer = new byte[500];
        int count;

        while ((count = in.read(buffer)) > 0) {
            bytes.write(buffer, 0, count);
        }
        in.close();
        return bytes.toByteArray();
    }

    protected String asUTF8(byte[] input) {
        return asUTF8(input, Integer.MAX_VALUE);
    }

    protected String asUTF8(byte[] input, int maxLen) {
        final String str = new String(input, StandardCharsets.UTF_8);
        if (str.length() < maxLen) {
            return str;
        }
        return str.substring(0, maxLen) + "[TRUNCATED...]";
    }
}
