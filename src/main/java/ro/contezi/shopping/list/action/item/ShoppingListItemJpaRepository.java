package ro.contezi.shopping.list.action.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.list.ShoppingListItem;

import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface ShoppingListItemJpaRepository extends JpaRepository<ShoppingListItem, String> {
    List<ShoppingListItem> findByShoppingList_idAndItemName(String shoppingListId, String itemName);
    Set<ShoppingListItem> findByShoppingList_AuthorOrShoppingList_Shares_Author(Author author, Author shareAuthor);
}
