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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationConfigurationServiceTest extends TestContainersSetup {

  @Autowired private RegisterAppService registerAppService;

  @Autowired private ApplicationConfigurationService applicationConfigurationService;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserGroupRelationRepository userGroupRelationRepository;

  @Autowired private GroupRepository groupRepository;

  @Test
  @DisplayName("Application Configuration was successfully processed")
  public void appCfgSuccessfulFlowTest() throws IOException {
    var app = new Application(1L, "MARI");
    registerAppService.registerApp(new RegisterAppDtoIn("MARI", "testUser"));
    String json =
        StreamUtils.copyToString(
            this.getClass().getResourceAsStream("/db/dataset/test_appcfg.json"),
            Charset.defaultCharset());
    ApplicationConfigurationDto dto =
        objectMapper.readValue(json, ApplicationConfigurationDto.class);
    var result = applicationConfigurationService.processAppConfig("testUser", dto);
    assertNotNull(result);
  }
}
