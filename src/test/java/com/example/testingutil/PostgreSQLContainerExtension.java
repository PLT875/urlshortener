package com.example.testingutil;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.util.concurrent.atomic.AtomicInteger;

public class PostgreSQLContainerExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String NAMESPACE_KEY = "postgresql_container_extension";
    private static final String STARTED_KEY = "started";
    private static final AtomicInteger globalClassCount = new AtomicInteger(0);

    @Override
    public void beforeAll(ExtensionContext context) {
        ExtensionContext.Store root = context.getRoot().getStore(ExtensionContext.Namespace.create(NAMESPACE_KEY));
        
        synchronized (globalClassCount) {
            if (globalClassCount.getAndIncrement() == 0) {
                PostgreSQLTestContainer.start();
                root.put(STARTED_KEY, true);
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // Do not stop the container after each test class
        // It will be stopped when the JVM shuts down
    }
}



