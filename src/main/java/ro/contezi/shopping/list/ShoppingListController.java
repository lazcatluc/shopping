package ro.contezi.shopping.list;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/lists")
public class ShoppingListController {
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListController(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @GetMapping("/{shoppingListId}")
    @ResponseBody
    public ResponseEntity<ShoppingList> getShoppingList(@PathVariable String shoppingListId) {
        ShoppingList shoppingList = shoppingListRepository.find(shoppingListId);
        if (shoppingList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shoppingList);
    }
}
