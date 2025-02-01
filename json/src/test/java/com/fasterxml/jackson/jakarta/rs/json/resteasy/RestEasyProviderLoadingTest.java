package com.fasterxml.jackson.jakarta.rs.json.resteasy;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.jakarta.rs.json.JakartaRSTestBase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// 30-May-2021, tatu: At this point (2.13, Jakarta-RS) I am not 100% sure
//   this actually proves much anything; but leaving in case it might help
//   catch some specific issues.
public class RestEasyProviderLoadingTest extends JakartaRSTestBase
{
    @Test
    public void testLoading() throws Exception
    {
        ResteasyJackson2Provider provider = new ResteasyJackson2Provider();
        assertNotNull(provider); // just to avoid compiler warning
    }
}
