package ro.contezi.shopping.config;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import ro.contezi.shopping.ReplySender;
import ro.contezi.shopping.config.ShoppingTest.ShoppingTestConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingTestConfig.class)
public class ShoppingTest {
    
    private static final Logger LOGGER = Logger.getLogger(ShoppingTest.class);
    
    @Autowired
    private Webhook webhook;

    @Configuration
    @Import(Shopping.class)
    public static class ShoppingTestConfig {
        @Bean
        public ReplySender replySender() {
            return LOGGER::info;
        }
    }
    
    @Test
    public void contextLoads() throws Exception {
        buildMessage("hello!");
        buildMessage("list");
        buildMessage("add cheese");
        buildMessage("add cheese");
        buildMessage("buy cheese");
        buildMessage("add cheese");
        buildMessage("add apples");
        buildMessage("remove cheese");
    }

    private void buildMessage(String message) throws Exception {
        webhook.receiveMessage("{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1508051848445,\"messaging\":"
                + "[{\"sender\":{\"id\":\"1513421495405103\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1508051847330,\"message\":"
                + "{\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"" + message + "\"}}]}]}");

        Thread.sleep(100);
    }
}
