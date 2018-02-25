package ro.contezi.shopping.integration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ro.contezi.shopping.ConfigurableUser;

import static org.assertj.core.api.Assertions.assertThat;

public class ShareListSpec extends SeleniumFixture {
    @Autowired
    @Qualifier("friend")
    private ConfigurableUser friend;

    private SeleniumFixture friendBrowser;


    @Override
    public void setUp() throws InterruptedException {
        super.setUp();
        typeMessageAndAwaitResponse("new");
        friendBrowser = new SeleniumFixture(this, friend);
        friendBrowser.setUp();
        friendBrowser.typeMessageAndAwaitResponse("hi");
    }

    @Override
    public void tearDown() throws InterruptedException {
        super.tearDown();
        friendBrowser.tearDown();
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstName() throws InterruptedException {
        typeMessageAndAwaitResponse("share with " + friend.getFirstName());
        typeMessageAndAwaitResponse("add cheese");
        friendBrowser.clickOnQuickReplyAndAwaitResponse("OK");

        assertThat(friendBrowser.lastMessage()).isEqualTo("cheese");
    }

    @Test
    public void acceptShareTriggersNotificationForSharer() throws InterruptedException {
        typeMessageAndAwaitResponse("share with " + friend.getFirstName());
        friendBrowser.clickOnQuickReplyAndAwaitResponse("OK");

        assertThat(lastMessage()).isEqualTo(friend.getFirstName() + " has accepted to share.");
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstNameLastNameNoCaps() throws InterruptedException {
        typeMessageAndAwaitResponse("share with " + friend.getFirstName().toLowerCase() + " " +
                friend.getLastName().toLowerCase());
        typeMessageAndAwaitResponse("add cheese");
        friendBrowser.clickOnQuickReplyAndAwaitResponse("OK");

        assertThat(friendBrowser.lastMessage()).isEqualTo("cheese");
    }

    @Test
    public void canRejectSharedList() throws InterruptedException {
        typeMessageAndAwaitResponse("share with " + friend.getFirstName() + " " + friend.getLastName());
        typeMessageAndAwaitResponse("add cheese");
        friendBrowser.clickOnQuickReplyAndAwaitResponse("No, thanks");

        assertThat(friendBrowser.lastMessage()).isNotEqualTo("cheese");
    }
}
