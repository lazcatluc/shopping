package ro.contezi.shopping.list;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import ro.contezi.shopping.list.InMemoryShoppingLists;

public class InMemoryShoppingListsTest {
    
    private InMemoryShoppingLists inMemoryShoppingLists;
    
    @Before
    public void setUp() {
        inMemoryShoppingLists = new InMemoryShoppingLists();
    }

    @Test
    public void holdsItemAdded() {
        inMemoryShoppingLists.add("1", "item");
        
        assertThat(inMemoryShoppingLists.toString()).contains("[item=false]");
    }

    @Test
    public void marksBoughtItem() {
        inMemoryShoppingLists.add("1", "item");
        inMemoryShoppingLists.buy("1", "item");
        
        assertThat(inMemoryShoppingLists.toString()).contains("[item=true]");
    }
    
    @Test
    public void removesItem() {
        inMemoryShoppingLists.add("1", "item");
        inMemoryShoppingLists.add("1", "another-item");
        inMemoryShoppingLists.remove("1", "item");
        
        assertThat(inMemoryShoppingLists.toString()).contains("[another-item=false]");
    }
}
