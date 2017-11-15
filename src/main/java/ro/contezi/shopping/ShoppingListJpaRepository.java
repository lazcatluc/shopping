package ro.contezi.shopping;

import java.util.Map;

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
    
    default Map<String, Boolean> get(String shoppingListId) {
        return findOne(shoppingListId).getItems();
    }
    
}
