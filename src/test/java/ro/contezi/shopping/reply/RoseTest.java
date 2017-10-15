package ro.contezi.shopping.reply;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class RoseTest {
    
    private static final Logger LOGGER = LogManager.getLogger(RoseTest.class);
    
    @Test
    public void canTalkToRose() throws Exception {
        LOGGER.info(new Rose("http://ec2-54-215-197-164.us-west-1.compute.amazonaws.com/ui.php", new RestTemplate()).reply("where are you?"));
    }
}
