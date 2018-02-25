package ro.contezi.shopping.list;

import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.contezi.shopping.author.Author;

@Repository
@Transactional
public interface ShoppingListJpaRepository extends JpaRepository<ShoppingList, String>, ShoppingListRepository {
    default ShoppingList find(String shoppingListId) {
        return findOne(shoppingListId);
    }

    default ShoppingListItem add(String shoppingListId, String item) {
        return findOne(shoppingListId).addItem(item);
    }
    
    default ShoppingListItem remove(String shoppingListId, String item) {
        return findOne(shoppingListId).removeItem(item);
    }
    
    default ShoppingListItem buy(String shoppingListId, String item) {
        return findOne(shoppingListId).buyItem(item);
    }
    
    default Set<ShoppingListItem> get(String shoppingListId) {
        return findOne(shoppingListId).getItems();
    }
    
    default ShoppingList share(String shoppingListId, Author author) {
        ShoppingList findOne = findOne(shoppingListId);
        findOne.shareWith(author);
        save(findOne);
        return findOne;
    }

    default ShoppingList saveShoppingList(ShoppingList list) {
        return save(list);
    }
    
}
