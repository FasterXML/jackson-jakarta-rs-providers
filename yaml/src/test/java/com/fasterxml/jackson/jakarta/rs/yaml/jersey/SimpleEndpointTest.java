package com.fasterxml.jackson.jakarta.rs.yaml.jersey;

import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jakarta.rs.yaml.dw.SimpleEndpointTestBase;

import jakarta.servlet.Servlet;

public class SimpleEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
