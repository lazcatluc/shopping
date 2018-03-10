package ro.contezi.shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.TargetMessages;
import ro.contezi.shopping.facebook.TargetedMessage;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.action.item.ShoppingListAction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingListTestConfig.class)
@ActiveProfiles("default")
public class ShoppingListTest {
    private static final Logger LOGGER = getLogger(ShoppingListTest.class);

    @Autowired
    private Users users;
    
    @Autowired
    private LatestList latestList;

    @Autowired
    private TargetMessages messages;

    @Autowired
    private ConfigurableUser defaultUser;

    private Users.Action user;

    @Before
    public void setUp() {
        user = users.user(defaultUser.getId());
        user.startsANewList();
    }

    @After
    public void after() {
        messages.clear();
    }

    @Test
    public void getsLatestList() {
        ShoppingList myList = latestList();
        
        assertThat(myList.getAuthor().getFirstName()).isEqualTo(defaultUser.getFirstName());
    }

    protected ShoppingList latestList() {
        return latestList.get(user.toString());
    }

    @Test
    public void processesListsBasedOnUserActions() {
        user.says("hello!")
            .displaysCurrentList()
            .adds("cheese")
            .adds("cheese")
            .buys("cheese")
            .adds("cheese")
            .adds("bread")
            .adds("apples")
            .removes("bread");

        assertLatestList()
            .contains("cheese=true")
            .contains("apples=false")
            .doesNotContain("bread");
        
        user.startsANewList();

        assertLatestList()
            .doesNotContain("cheese");
    }

    @Test
    public void addsCommaSeparatedItems() {
        user.adds("cheese, apples, bread");

        assertLatestList()
            .contains("cheese=false")
            .contains("apples=false")
            .contains("bread=false");
    }

    @Test
    public void normalizesUnicode() {
        assertThat(ShoppingListAction.removeUnicode("ăîȘâțș"))
                .isEqualTo("aiSats");
    }

    @Test
    public void handlesUnicode() {
        user.startsANewList();
        user.adds("ăîȘâțș");

        assertLatestList().contains("aiSats=false");
    }

    @Test
    public void sendsQuickReplyWhenPartialMatch() {
        user.startsANewList()
            .adds("hrana umeda")
            .adds("hrana uscata")
            .buys("hrana");

        assertLatestList()
            .contains("hrana umeda=false")
            .contains("hrana uscata=false")
            .doesNotContain("hrana=");
        messages.forEach(m -> LOGGER.info("{}", m));
        assertThat(messages.lastMessage()
                    .getQuickReplies()
                    .size())
                .isEqualTo(3);
//        assertThat(messages.lastMessage().getQuickReplies().toString()).doesNotContain("✔");
//
//        user.displaysCurrentList();
//        assertThat(messages.lastMessage().getQuickReplies()).isNullOrEmpty();
//
//        user.clicksOnQuickReply("buy hrana uscata");
//        assertLatestList()
//            .contains("hrana umeda=false")
//            .contains("hrana uscata=true")
//            .doesNotContain("hrana=");
//
//        user.buys("hrana");
//        assertThat(messages.lastMessage().getQuickReplies().toString())
//            .contains("hrana uscata ✔")
//            .doesNotContain("buy hrana uscata ✔");
//
//        user.clicksOnQuickReply("buy hrana");
//        assertLatestList()
//            .contains("hrana umeda=false")
//            .contains("hrana uscata=true")
//            .contains("hrana=true");
//
//        messages.forEach(m -> LOGGER.info("{}", m));
    }


    private AbstractCharSequenceAssert<?, String> assertLatestList() {
        return assertThat(latestList().toString());
    }

}
