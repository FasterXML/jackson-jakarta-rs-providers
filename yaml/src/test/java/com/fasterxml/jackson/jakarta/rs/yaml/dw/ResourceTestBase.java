package com.fasterxml.jackson.jakarta.rs.yaml.dw;

import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.ws.rs.core.Application;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.fasterxml.jackson.jakarta.rs.yaml.JacksonYAMLProvider;
import com.fasterxml.jackson.jakarta.rs.yaml.JakartaRSTestBase;

/**
 * Intermediate base for tests that run actual full JAX-RS resource.
 */
public abstract class ResourceTestBase extends JakartaRSTestBase
{
    protected static abstract class YAMLApplication extends Application
    {
        protected final Object[] _providers;
        protected final Object _resource;

        protected YAMLApplication(Object resource, Object... providers) {
            _providers = providers;
            _resource = resource;
        }
        
        @Override
        public Set<Object> getSingletons() {
            HashSet<Object> singletons = new HashSet<Object>();
            for(Object provider : _providers) {
                singletons.add(provider);
            }
            singletons.add(_resource);
            return singletons;
        }
    }

    protected static abstract class YAMLApplicationWithJackson extends YAMLApplication
    {
        public YAMLApplicationWithJackson(Object resource) {
            super(resource, new JacksonYAMLProvider());
        }
    }

    /*
    /**********************************************************************
    /* Abstract and overridable config methods
    /**********************************************************************
     */

    protected abstract Class<? extends Servlet> servletContainerClass();

    /*
    /**********************************************************************
    /* Starting actual JAX-RS container
    /**********************************************************************
     */
    
    protected Server startServer(int port, Class<? extends Application> appClass) {
        return startServer(port, appClass, null);
    }
    
    protected Server startServer(int port, Class<? extends Application> appClass,
            Class<? extends Filter> filterClass)
    {
        Server server = new Server(port);
        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);
        ServletHolder servlets = new ServletHolder(servletContainerClass());
        servlets.setInitParameter("jakarta.ws.rs.Application", appClass.getName());
        final ServletContextHandler mainHandler = new ServletContextHandler(contexts, "/", true, false);
        mainHandler.addServlet(servlets, "/*");

        if (filterClass != null) {
            mainHandler.addFilter(filterClass, "/*", java.util.EnumSet.allOf(DispatcherType.class));
        }
        
        server.setHandler(mainHandler);
        try {
            server.start();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return server;
    }
}
