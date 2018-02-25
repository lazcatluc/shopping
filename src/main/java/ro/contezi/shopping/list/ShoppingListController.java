package ro.contezi.shopping.list;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/users")
public class ShoppingListController {
    private final LatestList latestList;

    public ShoppingListController(LatestList latestList) {
        this.latestList = latestList;
    }

    @GetMapping("/{userId}/lists/latest")
    @ResponseBody
    public ShoppingList getShoppingList(@PathVariable String userId) {
        return latestList.get(userId);
    }
}
