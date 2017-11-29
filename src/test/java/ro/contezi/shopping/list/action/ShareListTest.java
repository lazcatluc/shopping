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
import ro.contezi.shopping.ShoppingListTestConfig;
import ro.contezi.shopping.Users;
import ro.contezi.shopping.facebook.TargetedMessage;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingListTestConfig.class)
public class ShareListTest {
    private static final Logger LOGGER = getLogger(ShareListTest.class);

    private Users.Action sender;
    private Users.Action receiver;
    
    @Autowired
    private Users users;
    
    @Autowired
    private LatestList latestList;

    @Autowired
    private List<TargetedMessage> messages;

    @After
    public void after() {
        messages.forEach(message -> LOGGER.info("{}", message));
        messages.clear();
    }

    @Before
    public void sayHi() throws Exception {
        sender = users.user("1464923436924425");
        receiver = users.user("1513421495405103");
        sender.says("Hi");
        receiver.says("Hi");
        sender.startsANewList();
    }
    
    @Test
    public void sharesListBetweenTwoUsersByFirstName() throws Exception {
        sender.sharesListWith("Catalin");
        receiver.acceptsSharedList(latestList(sender));

        assertThat(latestList(sender).getShares().size()).isEqualTo(1);
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstNameLastNameNoCaps() throws Exception {
        sender.sharesListWith("catalin lazar");
        receiver.acceptsSharedList(latestList(sender));

        assertThat(latestList(sender).getShares().size()).isEqualTo(1);
    }
    
    @Test
    public void canRejectSharedList() throws Exception {
        sender.sharesListWith("Catalin");
        receiver.rejectsSharedList(latestList(sender));

        assertThat(latestList(sender).getShares()).isEmpty();
    }
    
    @Test
    public void sharedListItemsAreSeenByTheNewUser() throws Exception {
        sender.sharesListWith("Catalin")
            .adds("cheese");
        receiver.acceptsSharedList(latestList(sender));

        assertThat(latestList(receiver).toString()).contains("cheese=false");
        
    }

    private ShoppingList latestList(Users.Action user) {
        return latestList.get(user.toString());
    }
}
