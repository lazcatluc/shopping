package ro.contezi.shopping.integration;

import java.io.IOException;
import org.junit.Ignore;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import ro.contezi.shopping.Shopping;
import ro.contezi.shopping.GraphApi;
import ro.contezi.shopping.reply.FacebookReplySender;
import ro.contezi.shopping.reply.ReplySender;

@Configuration
@Import(Shopping.class)
public class ShoppingIntegration {
    @Value("${server.port}")
    private int serverPort;
    @Value("${chrome.driver.path:chromedriver.exe}")
    private String chromeDriverPath;
    @Value("${facebook.graph.api.url}")
    private String graphApiUrl;
    @Value("${facebook.token}")
    private String facebookToken;

    @Bean
    @Profile("itest")
    public Ngrok ngrok() throws IOException, InterruptedException {
        return new Ngrok(serverPort);
    }

    @Bean(destroyMethod = "close")
    @Profile("itest")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        return new ChromeDriver(new ChromeDriverService.Builder()
                .usingAnyFreePort().build());
    }

    @Bean
    @Profile("itest")
    public ReplySender replySender(RestTemplate restTemplate) {
        return new FacebookReplySender(restTemplate, facebookToken, graphApiUrl);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try (ConfigurableApplicationContext itestContext = SpringApplication.run(ShoppingIntegration.class,
                "--spring.profiles.active=itest")) {
            GraphApi graphApi = itestContext.getBean(GraphApi.class);
            Ngrok ngrok = itestContext.getBean(Ngrok.class);
            graphApi.registerWebhook(ngrok.getUrl() + "/webhook");
            itestContext.getBeansOfType(SeleniumFixture.class).values().stream()
                    .filter(seleniumFixture -> seleniumFixture.getClass()
                            .getAnnotation(Ignore.class) == null)
                    .forEach(SeleniumFixture::test);
        }
    }
}
