package ro.contezi.shopping.integration;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.contezi.shopping.ConfigurableUser;

@Component
@Profile("itest")
public class UserSaysHi implements SeleniumFixture {
    private static final Logger LOGGER = getLogger(UserSaysHi.class);
    private final WebDriver webDriver;
    private final String baseUrl;
    private final ConfigurableUser user;

    @Autowired
    public UserSaysHi(WebDriver webDriver, @Value("${facebook.messenger.url}") String baseUrl,
                      ConfigurableUser user) {
        this.webDriver = webDriver;
        this.baseUrl = baseUrl;
        this.user = user;
    }

    @Override
    public void test() {
        webDriver.get(baseUrl);
        login(user);
        sendMessage("Hello");
        LOGGER.info(getAllMessages().toString());
        webDriver.quit();
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }
}
