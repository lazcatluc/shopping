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
        friendBrowser = new SeleniumFixture(this, friend);
        friendBrowser.setUp();
        typeMessageAndAwaitResponse("new");
    }

    @Override
    public void tearDown() {
        super.tearDown();
        friendBrowser.tearDown();
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstName() throws InterruptedException {
        typeMessageAndAwaitResponse("share with " + friend.getFirstName());
        friendBrowser.clickOnQuickReplyAndAwaitResponse("OK");
        typeMessageAndAwaitResponse("add cheese");

        assertThat(friendBrowser.lastMessage()).isEqualTo("cheese");
    }

}
