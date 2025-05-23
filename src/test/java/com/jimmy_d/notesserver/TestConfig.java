package com.jimmy_d.notesserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy_d.notesserver.integration.RestTestUtils;
import com.jimmy_d.notesserver.integration.TestFactory;
import com.jimmy_d.notesserver.mapper.UserReadMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration(proxyBeanMethods = false)
public class TestConfig {
    @Bean
    public RestTestUtils restTestUtils(MockMvc mockMvc,
                                       ObjectMapper objectMapper,
                                       TestFactory testFactory,
                                       UserReadMapper userReadMapper) {
        return new RestTestUtils(mockMvc, objectMapper, testFactory, userReadMapper);
    }
}
