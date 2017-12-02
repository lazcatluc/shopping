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

    @Override
    public void setUp() throws InterruptedException {
        super.setUp();
        typeMessageAndAwaitResponse("new");
        switchToUser(friend);
        typeMessageAndAwaitResponse("hi");
        switchToUser();
    }

    @Test
    public void sharesListBetweenTwoUsersByFirstName() throws InterruptedException {
        typeMessageAndAwaitResponse("share with " + friend.getFirstName());
        typeMessageAndAwaitResponse("add cheese");
        switchToUser(friend);
        clickOnQuickReplyAndAwaitResponse("OK");

        assertThat(lastMessage()).isEqualTo("cheese");
    }

}
