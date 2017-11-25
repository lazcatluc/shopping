package ro.contezi.shopping;

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
import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.TargetedMessage;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.reply.ShoppingListAction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingTestConfig.class)
public class ShoppingTest {
    private static final Logger LOGGER = getLogger(ShoppingTest.class);

    @Autowired
    private Webhook webhook;
    
    @Autowired
    private LatestList latestList;

    @Autowired
    private List<TargetedMessage> messages;
    
    private final String facebookPageUserId = "1513421495405103";

    @Before
    public void setUp() throws Exception {
        buildMessage("new");
    }

    @After
    public void after() {
        messages.clear();
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

    @Test
    public void addsCommaSeparatedItems() throws Exception {
        buildMessage("add cheese, apples, bread");

        ShoppingList myList = latestList.get(facebookPageUserId);
        assertThat(myList.toString()).contains("cheese=false").contains("apples=false").contains("bread=false");
    }

    @Test
    public void normalizesUnicode() throws Exception {
        assertThat(ShoppingListAction.removeUnicode("ăîȘâțș"))
                .isEqualTo("aiSats");
    }

    @Test
    public void handlesUnicode() throws Exception {
        buildMessage("new");
        buildMessage("add ăîȘâțș");

        ShoppingList myList = latestList.get(facebookPageUserId);
        assertThat(myList.toString()).contains("aiSats=false");
    }

    @Test
    public void sendsQuickReplyWhenPartialMatch() throws Exception {
        buildMessage("new");
        buildMessage("add hrana umeda");
        buildMessage("add hrana uscata");
        buildMessage("buy hrana");
        assertThat(latestList.get(facebookPageUserId).toString()).contains("hrana umeda=false")
            .contains("hrana uscata=false").doesNotContain("hrana=");
        assertThat(lastMessage().getQuickReplies().size()).isEqualTo(3);
        assertThat(lastMessage().getQuickReplies().toString()).doesNotContain("✔");
        buildMessage("list");
        assertThat(lastMessage().getQuickReplies()).isNullOrEmpty();
        buildQuickReply("buy hrana uscata");
        assertThat(latestList.get(facebookPageUserId).toString()).contains("hrana umeda=false")
            .contains("hrana uscata=true").doesNotContain("hrana=");
        buildMessage("buy hrana");
        assertThat(lastMessage().getQuickReplies().toString()).contains("hrana uscata ✔")
            .doesNotContain("buy hrana uscata ✔");
        buildQuickReply("buy hrana");
        assertThat(latestList.get(facebookPageUserId).toString()).contains("hrana umeda=false")
            .contains("hrana uscata=true").contains("hrana=true");
        LOGGER.info("{}", messages);
    }

    protected FacebookMessage lastMessage() {
        return messages.get(messages.size() - 1).getMessage();
    }

    private void buildMessage(String message) throws Exception {
        webhook.receiveMessage(ShoppingTestConfig.mesageFromUser(message, facebookPageUserId));

        Thread.sleep(1000);
    }

    private void buildQuickReply(String message) throws Exception {
        webhook.receiveMessage(ShoppingTestConfig.quickReplyFromUser(message, facebookPageUserId));

        Thread.sleep(1000);
    }
}
