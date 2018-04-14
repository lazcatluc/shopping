package ro.contezi.shopping.list.action.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.contezi.shopping.list.ShoppingListItem;

import java.util.List;

@Repository
@Transactional
public interface ShoppingListItemJpaRepository extends JpaRepository<ShoppingListItem, String> {
    List<ShoppingListItem> findByShoppingList_idAndItemName(String shoppingListId, String itemName);
}
