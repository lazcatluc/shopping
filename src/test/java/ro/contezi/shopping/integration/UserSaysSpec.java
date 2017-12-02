package ro.contezi.shopping.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class UserSaysSpec extends SeleniumFixture {

    @Test
    public void hello() throws InterruptedException {
        typeMessage("Hello");

        assertThat(penultimateMessage()).isEqualTo("Hello");
    }

    @Test
    public void newList() throws InterruptedException {
        typeMessage("new");

        assertThat(lastMessage()).isEqualTo("-");
    }
}
