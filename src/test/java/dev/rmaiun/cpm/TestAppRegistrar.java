package dev.rmaiun.cpm;

import static dev.rmaiun.cpm.dto.DomainAuthorizationType.RW;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.dto.ApplicationConfigurationDto;
import dev.rmaiun.cpm.dto.DomainConfigurationDto;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.service.ApplicationConfigurationService;
import dev.rmaiun.cpm.service.RegisterAppService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestAppRegistrar {

  public static final String CPM_TEST_APP = "cpmTestApp";
  public static final String CPM_TEST_DOMAIN = "cpmTest";
  public static final String CPM_TEST_GROUP = "TestUsers";
  public static final String CPM_TEST_APP_OWNER = "user#0";
  public static final String CPM_TEST_APP_DEFAULT_USER1 = "user#1";
  public static final String CPM_TEST_APP_DEFAULT_USER2 = "user#2";

  @Autowired private RegisterAppService registerAppService;

  @Autowired private ApplicationConfigurationService applicationConfigurationService;

  @Autowired private ApplicationRepository applicationRepository;

  public Application registerTestApp() {
    registerAppService.registerApp(new RegisterAppDtoIn(CPM_TEST_APP, CPM_TEST_APP_OWNER));
    var domains =
        List.of(
            new DomainConfigurationDto(CPM_TEST_DOMAIN, null, null, Map.of(CPM_TEST_GROUP, RW)));
    var cfgDto =
        new ApplicationConfigurationDto(
            CPM_TEST_APP,
            true,
            domains,
            Map.of(
                CPM_TEST_GROUP, List.of(CPM_TEST_APP_DEFAULT_USER1, CPM_TEST_APP_DEFAULT_USER2)));
    applicationConfigurationService.processAppConfig(CPM_TEST_APP_OWNER, cfgDto);
    return applicationRepository
        .getByCode(CPM_TEST_APP)
        .orElseThrow(() -> new IllegalArgumentException("Test App should be present"));
  }
}
