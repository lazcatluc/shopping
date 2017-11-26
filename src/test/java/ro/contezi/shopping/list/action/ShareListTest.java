package ro.contezi.shopping.list.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.contezi.shopping.ShoppingTestConfig;
import ro.contezi.shopping.facebook.TargetedMessage;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingTestConfig.class)
public class ShareListTest {
    private static final Logger LOGGER = getLogger(ShareListTest.class);

    private final String user = "1464923436924425";
    private final String anotherUser = "1513421495405103";
    
    @Autowired
    private Webhook webhook;
    
    @Autowired
    private LatestList latestList;

    @Autowired
    private List<TargetedMessage> messages;

    @After
    public void after() {
        messages.forEach(message ->
                LOGGER.info("{}", message));
        messages.clear();
    }

    @Before
    public void sayHi() throws Exception {
        buildMessage("Hi", user);
        buildMessage("Hi", anotherUser);
        buildMessage("new", user);
    }
    
    @Test
    public void sharesListBetweenTwoUsersByFirstName() throws Exception {
        buildMessage("share with Catalin", user);
        ShoppingList list = latestList.get(user);
        buildQuickReply("accept_share " + list.getId(), anotherUser);
        
        assertThat(latestList.get(user).getShares().size()).isEqualTo(1);
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstNameLastNameNoCaps() throws Exception {
        buildMessage("share with catalin lazar", user);
        ShoppingList list = latestList.get(user);
        buildQuickReply("accept_share " + list.getId(), anotherUser);
        
        assertThat(latestList.get(user).getShares().size()).isEqualTo(1);
    }
    
    @Test
    public void canRejectSharedList() throws Exception {
        buildMessage("share with Catalin", user);
        ShoppingList list = latestList.get(user);
        buildQuickReply("reject_share " + list.getId(), anotherUser);
        
        assertThat(latestList.get(user).getShares()).isEmpty();
    }
    
    @Test
    public void sharedListIsSeenByTheNewUser() throws Exception {
        buildMessage("share with Catalin", user);
        ShoppingList list = latestList.get(user);
        buildMessage("add cheese", user);
        buildQuickReply("accept_share " + list.getId(), anotherUser);
        
        ShoppingList myList = latestList.get(anotherUser);
        assertThat(myList.toString()).contains("cheese=false");
        
    }

    private void buildMessage(String message, String user) throws Exception {
        webhook.receiveMessage(ShoppingTestConfig.mesageFromUser(message, user));

        Thread.sleep(1000);
    }

    private void buildQuickReply(String message, String user) throws Exception {
        webhook.receiveMessage(ShoppingTestConfig.quickReplyFromUser(message, user));

        Thread.sleep(1000);
    }
}
