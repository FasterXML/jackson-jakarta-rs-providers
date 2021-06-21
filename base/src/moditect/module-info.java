// Originally generated using Moditect maven plugin, last mod 14-Oct-2020
module com.fasterxml.jackson.jakarta.rs.base {
    exports com.fasterxml.jackson.jakarta.rs.annotation;
    exports com.fasterxml.jackson.jakarta.rs.base;
    exports com.fasterxml.jackson.jakarta.rs.base.util;
    exports com.fasterxml.jackson.jakarta.rs.cfg;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires transitive jakarta.ws.rs;
}
