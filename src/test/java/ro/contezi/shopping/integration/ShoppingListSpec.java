package ro.contezi.shopping.integration;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingListSpec extends SeleniumFixture {

    @Override
    public void setUp() throws InterruptedException {
        super.setUp();
        typeMessageAndAwaitResponse("new");
    }

    @Test
    public void addsToList() throws InterruptedException {
        typeMessageAndAwaitResponse("add cheese");

        assertThat(lastMessage()).isEqualTo("cheese");
    }

    @Test
    public void addsMultipleElementsToList() throws InterruptedException {
        typeMessageAndAwaitResponse("add cheese");
        typeMessageAndAwaitResponse("add bread");
        typeMessageAndAwaitResponse("add apples");

        assertThat(lastMessage()).contains("cheese", "apples", "bread");
    }

    @Test
    public void buysAddedElements() throws InterruptedException {
        typeMessageAndAwaitResponse("add cheese");
        typeMessageAndAwaitResponse("buy cheese");

        assertThat(lastMessage()).isEqualTo("cheese ✔");
    }

}
