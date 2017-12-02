package ro.contezi.shopping.integration;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ShoppingListSpec extends SeleniumFixture {

    @Override
    public void setUp() throws InterruptedException {
        super.setUp();
        typeMessage("new");
    }

    @Test
    public void addsToList() throws InterruptedException {
        typeMessage("add cheese");

        Assertions.assertThat(lastMessage()).isEqualTo("cheese");
    }

    @Test
    public void addsMultipleElementsToList() throws InterruptedException {
        typeMessage("add cheese");
        typeMessage("add bread");
        typeMessage("add apples");

        Assertions.assertThat(lastMessage()).contains("cheese", "apples", "bread");
    }

    @Test
    public void buysAddedElements() throws InterruptedException {
        typeMessage("add cheese");
        typeMessage("buy cheese");

        Assertions.assertThat(lastMessage()).isEqualTo("cheese ✔");
    }

}
