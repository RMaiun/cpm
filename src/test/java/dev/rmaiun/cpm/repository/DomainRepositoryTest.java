package dev.rmaiun.cpm.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.Domain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(
        scripts = {
            "/db/scripts/V1__create_schema_cpm.sql",
            "/db/migration/V1__create_app_table.sql",
            "/db/migration/V2__create_domain_table.sql"
        },
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class DomainRepositoryTest extends TestContainersSetup {

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private DomainRepository domainRepo;

    @BeforeEach
    @AfterEach
    public void setup() {
        domainRepo.clearTable();
        appRepo.clearTable();
    }

    @Test
    @DisplayName("Impossible to store domain without app")
    public void errorDomainWithoutApp() {
        var domain = new Domain(1L, "test", 3L);
        try {
            domainRepo.save(domain);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof DataIntegrityViolationException);
        }
    }

    @Test
    @DisplayName("CRUD Domain behavior is correct")
    public void domainSuccessfulSave() {
        var app = new Application(1L, "test");
        var appId = appRepo.save(app);
        var domain = new Domain(1L, "testDomain", appId);
        var domId = domainRepo.save(domain);
        var foundDomain = domainRepo.getById(domId);
        assertTrue(foundDomain.isPresent());
        var foundDom = foundDomain.get();
        assertEquals(domain.code(), foundDom.code());
        var foundList = domainRepo.listAll();
        assertThat(foundList, hasSize(1));
        var removedNumbers = domainRepo.delete(foundDom.id());
        assertEquals(1L, removedNumbers);
    }
}
