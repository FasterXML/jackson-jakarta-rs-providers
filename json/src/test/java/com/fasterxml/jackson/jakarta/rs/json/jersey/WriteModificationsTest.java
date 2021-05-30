package com.fasterxml.jackson.jakarta.rs.json.jersey;

import jakarta.servlet.Servlet;

import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jakarta.rs.json.dw.WriteModificationsTestBase;

public class WriteModificationsTest extends WriteModificationsTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
