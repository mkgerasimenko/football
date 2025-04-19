package com.football.listeners;

import com.football.components.API;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.*;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.football.config.APIConfig.CONFIG;
import static io.restassured.RestAssured.requestSpecification;
import static java.util.Optional.ofNullable;

/**
 * Main cleanup listener for API configuration and cleanup.
 */
@SuppressWarnings("FinalLocalVariable")
public class APIListener implements ISuiteListener, IInvokedMethodListener, ITestListener {

    private static final ThreadLocal<Cache<Class<? extends API>, API>> COMPONENT_CACHE = new ThreadLocal<>();
    private static final ConcurrentMap<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();

    public static Optional<Cache<Class<? extends API>, API>> getComponentCache() {
        return ofNullable(COMPONENT_CACHE.get());
    }

    @Override
    public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult) {
        if (method.isTestMethod()) {
            createComponentCache();
        }
    }

    @Override
    public void afterInvocation(final IInvokedMethod method, final ITestResult result) {
        if (method.isTestMethod()) {
            cleanupComponentCache();
        }
    }

    @Override
    public void onStart(final ISuite suite) {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(CONFIG.apiUrl())
                .addHeader("Content-Type", "application/json")
                .addFilter(new AllureRestAssured())
                .build();
    }

    @Override
    public void onTestStart(ITestResult result) {
        var methodName = result.getMethod().getMethodName();
        var key = result.getTestClass().getName() + "#" + methodName;
        var index = COUNTERS
                .computeIfAbsent(key, k -> new AtomicInteger(0))
                .getAndIncrement();

        var newName = String.format("%s[%d]", methodName, index);

        result.setTestName(newName);

        Allure.getLifecycle().updateTestCase(tc -> {
            tc.setName(newName);
            tc.setHistoryId(newName);
        });
    }

    private void createComponentCache() {
        COMPONENT_CACHE.set(Caffeine.newBuilder().build());
    }

    private void cleanupComponentCache() {
        getComponentCache().ifPresent(Cache::invalidateAll);
        COMPONENT_CACHE.remove();
    }
}
