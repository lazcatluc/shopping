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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.contezi.shopping.ConfigurableUser;
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

    @Autowired
    private ConfigurableUser defaultUser;

    @Autowired
    @Qualifier("friend")
    private ConfigurableUser friend;

    @After
    public void after() {
        messages.forEach(message -> LOGGER.info("{}", message));
        messages.clear();
    }

    @Before
    public void sayHi() {
        sender = users.user(friend.getId());
        receiver = users.user(defaultUser.getId());
        sender.says("Hi");
        receiver.says("Hi");
        sender.startsANewList();
    }
    
    @Test
    public void sharesListBetweenTwoUsersByFirstName() {
        sender.sharesListWith(defaultUser.getFirstName());
        receiver.acceptsSharedList(latestList(sender));

        assertThat(latestList(sender).getShares().size()).isEqualTo(1);
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstNameLastNameNoCaps() {
        sender.sharesListWith(defaultUser.getFirstName().toLowerCase() + " " +
                defaultUser.getLastName().toLowerCase());
        receiver.acceptsSharedList(latestList(sender));

        assertThat(latestList(sender).getShares().size()).isEqualTo(1);
    }
    
    @Test
    public void canRejectSharedList() {
        sender.sharesListWith(defaultUser.getFirstName());
        receiver.rejectsSharedList(latestList(sender));

        assertThat(latestList(sender).getShares()).isEmpty();
    }
    
    @Test
    public void sharedListItemsAreSeenByTheNewUser() {
        sender.sharesListWith(defaultUser.getFirstName())
            .adds("cheese");
        receiver.acceptsSharedList(latestList(sender));

        assertThat(latestList(receiver).toString()).contains("cheese=false");
        
    }

    private ShoppingList latestList(Users.Action user) {
        return latestList.get(user.toString());
    }
}
