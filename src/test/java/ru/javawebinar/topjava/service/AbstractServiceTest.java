package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.TimingRules;

import static org.hamcrest.CoreMatchers.instanceOf;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {

    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;

    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    //  Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        try {
            runnable.run();
            System.out.println("_____________1__" + exceptionClass.getName());
            Assert.fail("Expected " + exceptionClass.getName());
            System.out.println("_____________2__" + exceptionClass.getName());
        } catch (Exception e) {
            Assert.assertThat(getRootCause(e), instanceOf(exceptionClass));
        }
    }

}
