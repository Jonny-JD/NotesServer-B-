package com.jimmy_d.notesserver.integration;

import com.jimmy_d.notesserver.TestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestConfig.class)
@WithMockUser(username = "Dummy_user_1", password = "dummy_1_pass", authorities = {"USER", "ADMIN"})
public class ControllerTestBase extends IntegrationTestBase {
}
