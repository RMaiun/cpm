package dev.rmaiun.cpm.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.dto.ApplicationConfigurationDto;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(
        scripts = {
            "/db/scripts/V1__create_schema_cpm.sql",
            "/db/migration/V1__create_app_table.sql",
            "/db/migration/V2__create_domain_table.sql",
            "/db/migration/V3__create_business_role_table.sql",
            "/db/migration/V4__create_business_group_table.sql",
            "/db/migration/V5__create_group_role_table.sql",
            "/db/migration/V6__create_user_group_table.sql",
            "/db/migration/V7__create_domain_to_domain_table.sql"
        },
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ApplicationConfigurationServiceTest extends TestContainersSetup {

    @Autowired
    private RegisterAppService registerAppService;

    @Autowired
    private ApplicationConfigurationService applicationConfigurationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserGroupRelationRepository userGroupRelationRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    @AfterEach
    @Sql(scripts = {"/db/scripts/V0__clean_env.sql"})
    public void setup() {}

    @Test
    @DisplayName("Application Configuration was successfully processed")
    public void appCfgSuccessfulFlowTest() throws IOException {
        var app = new Application(1L, "MARI");
        registerAppService.registerApp(new RegisterAppDtoIn("MARI", "testUser"));
        String json = StreamUtils.copyToString(
                this.getClass().getResourceAsStream("/db/dataset/test_appcfg.json"), Charset.defaultCharset());
        ApplicationConfigurationDto dto = objectMapper.readValue(json, ApplicationConfigurationDto.class);
        var result = applicationConfigurationService.processAppConfig("testUser", dto);
        assertNotNull(result);
    }
}
