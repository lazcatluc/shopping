package ro.contezi.shopping.integration;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ro.contezi.shopping.info.ApiInfo;

public class InfoControllerSpec extends SeleniumFixture {
    @Value("${server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void infoShouldReturnFormattedMavenVersion() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:{port}/info",
                ApiInfo.class, port).getAppVersion()).matches("[0-9]+\\.[0-9]+\\.[0-9]+(-SNAPSHOT)?\\.[0-9]{14}");
    }

}
