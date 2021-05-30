package com.fasterxml.jackson.jakarta.rs.json.jersey;

import jakarta.servlet.Servlet;

import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jakarta.rs.json.dw.AnnotationTestBase;

public class AnnotationTest extends AnnotationTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
