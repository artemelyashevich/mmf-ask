package com.elyashevich.mmfask.service;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Tag("integration")
public abstract class BaseIntegrationTest {
    // Shared context for all integration tests
}
