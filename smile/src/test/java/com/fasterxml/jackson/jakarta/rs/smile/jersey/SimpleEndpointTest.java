package com.fasterxml.jackson.jakarta.rs.smile.jersey;

import jakarta.servlet.Servlet;

import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jakarta.rs.smile.dw.SimpleEndpointTestBase;

public class SimpleEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
