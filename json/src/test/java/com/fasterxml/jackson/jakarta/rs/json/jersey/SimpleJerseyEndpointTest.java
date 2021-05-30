package com.fasterxml.jackson.jakarta.rs.json.jersey;

import jakarta.servlet.Servlet;

import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jakarta.rs.json.dw.SimpleEndpointTestBase;

public class SimpleJerseyEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
