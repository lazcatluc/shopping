package ro.contezi.shopping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.contezi.shopping.facebook.TargetedMessage;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingTestConfig.class)
public class ShoppingTest {
      
    @Autowired
    private Webhook webhook;
    
    @Autowired
    private LatestList latestList;

    @Autowired
    private List<TargetedMessage> messages;
    
    private final String facebookPageUserId = "1513421495405103";

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

    private void buildMessage(String message) throws Exception {
        webhook.receiveMessage(ShoppingTestConfig.mesageFromUser(message, facebookPageUserId));

        Thread.sleep(200);
    }
}
