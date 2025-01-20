// Jakarta-RS Base module-info for Main artifact
module tools.jackson.jakarta.rs.base
{
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;

    requires transitive jakarta.ws.rs;

    exports tools.jackson.jakarta.rs.annotation;
    exports tools.jackson.jakarta.rs.base;
    exports tools.jackson.jakarta.rs.base.util;
    exports tools.jackson.jakarta.rs.cfg;
}
