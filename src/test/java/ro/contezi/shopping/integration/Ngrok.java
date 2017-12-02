package ro.contezi.shopping.integration;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Ngrok {
    private static final Logger LOGGER = getLogger(Ngrok.class);
    private final Process ngrok;
    private final String url;

    public Ngrok(int serverPort, String ngrokExecutable) throws IOException {
        ngrok = new ProcessBuilder(ngrokExecutable, "http", "-bind-tls=true",
                String.valueOf(serverPort)).start();
        try {
            Thread.sleep(3000);
            ResponseEntity<String> output = new RestTemplate().getForEntity("http://localhost:4040/status",
                    String.class);
            Matcher matcher = Pattern.compile("https://[0-9a-z]+\\.ngrok\\.io").matcher(output.getBody());

            if (!matcher.find()) {
                throw new IllegalStateException("Cannot find ngrok url");
            }
            url = matcher.group();
            LOGGER.info("Started ngrok with URL {}", url);
        }
        catch (InterruptedException | RuntimeException e) {
            shutdown();
            throw new ExceptionInInitializerError(e);
        }
    }

    @PreDestroy
    public void shutdown() {
        ngrok.destroy();
        LOGGER.info("Ngrok shut down");
    }

    public String getUrl() {
        return url;
    }
}
