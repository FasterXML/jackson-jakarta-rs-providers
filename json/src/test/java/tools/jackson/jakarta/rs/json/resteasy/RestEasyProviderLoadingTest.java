package tools.jackson.jakarta.rs.json.resteasy;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import tools.jackson.jakarta.rs.json.JakartaRSTestBase;

// 30-May-2021, tatu: At this point (2.13, Jakarta-RS) I am not 100% sure
//   this actually proves much anything; but leaving in case it might help
//   catch some specific issues.
public class RestEasyProviderLoadingTest extends JakartaRSTestBase
{
    public void testLoading() throws Exception
    {
     // 13-Aug-2022, tatu: Won't work with Jackson 3.x, so comment out
        ResteasyJackson2Provider provider = null; // new ResteasyJackson2Provider();
//        assertNotNull(provider); // just to avoid compiler warning
    }
}
