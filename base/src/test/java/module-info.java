// Jakarta-RS Base module-info for (unit) Tests
module tools.jackson.jakarta.rs.base
{
    // Since we are not split from Main artifact, will not
    // need to depend on Main artifact -- but need its dependencies

    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;

    // Additional test lib/framework dependencies
    requires org.junit.jupiter.api;

    // Further, need to open up test packages for JUnit et al
    opens tools.jackson.jakarta.rs.base;
    opens tools.jackson.jakarta.rs.base.cfg;
}
