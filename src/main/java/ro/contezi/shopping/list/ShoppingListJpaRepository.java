package ro.contezi.shopping.list;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListJpaRepository extends JpaRepository<ShoppingList, String>, ShoppingListRepository {
    default void add(String shoppingListId, String item) {
        findOne(shoppingListId).addItem(item);
    }
    
    default void remove(String shoppingListId, String item) {
        findOne(shoppingListId).removeItem(item);
    }
    
    default void buy(String shoppingListId, String item) {
        findOne(shoppingListId).buyItem(item);
    }
    
    default Set<ShoppingListItem> get(String shoppingListId) {
        return findOne(shoppingListId).getItems();
    }
    
}
