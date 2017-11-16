package ro.contezi.shopping;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import ro.contezi.shopping.ShoppingTest.ShoppingTestConfig;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.reply.ReplySender;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingTestConfig.class)
public class ShoppingTest {
    
    private static final Logger LOGGER = Logger.getLogger(ShoppingTest.class);
    
    @Autowired
    private Webhook webhook;
    
    @Autowired
    private LatestList latestList;
    
    private final String facebookPageUserId = "1513421495405103";

    @Configuration
    @Import(Shopping.class)
    public static class ShoppingTestConfig {
        @Bean
        public ReplySender replySender() {
            return LOGGER::info;
        }
    }
    
    @Test
    public void getsLatestList() throws Exception {
        ShoppingList myList = latestList.get(facebookPageUserId);
        
        assertThat(myList.getAuthor().getFirstName()).isEqualTo("Catalin");
    }
    
    @Test
    public void processesListsBasedOnMessages() throws Exception {
        buildMessage("hello!");
        buildMessage("list");
        buildMessage("add cheese");
        buildMessage("add cheese");
        buildMessage("buy cheese");
        buildMessage("add cheese");
        buildMessage("add bread");
        buildMessage("add apples");
        buildMessage("remove bread");
        
        ShoppingList myList = latestList.get(facebookPageUserId);
        assertThat(myList.toString()).contains("cheese=true").contains("apples=false").doesNotContain("bread");
        
        buildMessage("new");
        myList = latestList.get(facebookPageUserId);
        assertThat(myList.toString()).doesNotContain("cheese");
    }

    private void buildMessage(String message) throws Exception {
        webhook.receiveMessage("{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1508051848445,\"messaging\":"
                + "[{\"sender\":{\"id\":\"" + facebookPageUserId + "\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1508051847330,\"message\":"
                + "{\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"" + message + "\"}}]}]}");

        Thread.sleep(200);
    }
}
