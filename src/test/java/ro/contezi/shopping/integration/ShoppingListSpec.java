package ro.contezi.shopping.integration;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ro.contezi.shopping.list.ShoppingList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ShoppingListSpec extends SeleniumFixture {
    @Value("${server.port}")
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

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
        ShoppingList shoppingList = testRestTemplate.getForObject("http://localhost:{port}/users/{userId}/lists/latest",
                ShoppingList.class, port, getUser().getId());
        String response = testRestTemplate.getForObject("http://localhost:{port}/lists/{shoppingListId}",
                String.class, port, shoppingList.getId());
        assertThat(response).contains("cheese", "apples", "bread");
    }

    @Test
    public void buysAddedElements() throws InterruptedException {
        typeMessageAndAwaitResponse("add cheese");
        typeMessageAndAwaitResponse("buy cheese");

        assertThat(lastMessage()).isEqualTo("cheese ✔");
    }

}
