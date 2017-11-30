package ro.contezi.shopping.integration;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ro.contezi.shopping.Shopping;
import ro.contezi.shopping.GraphApi;

@Configuration
@Import(Shopping.class)
public class ShoppingIntegration {
    @Value("${server.port}")
    private int serverPort;
    @Value("${chrome.driver.path:chromedriver.exe}")
    private String chromeDriverPath;

    @Bean
    @Profile("itest")
    public Ngrok ngrok(GraphApi graphApi) throws IOException, InterruptedException {
        return new Ngrok(serverPort);
    }

    @Bean
    @Profile("itest")
    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        return new ChromeDriver(new ChromeDriverService.Builder()
                .usingAnyFreePort()
                .build());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try (ConfigurableApplicationContext itestContext = SpringApplication.run(ShoppingIntegration.class,
                "--spring.profiles.active=itest")) {
            GraphApi graphApi = itestContext.getBean(GraphApi.class);
            Ngrok ngrok= itestContext.getBean(Ngrok.class);
            graphApi.registerWebhook(ngrok.getUrl() + "/webhook");
        }
    }
}
