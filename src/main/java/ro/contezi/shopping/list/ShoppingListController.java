package ro.contezi.shopping.list;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class ShoppingListController {
    private final LatestList latestList;

    public ShoppingListController(LatestList latestList) {
        this.latestList = latestList;
    }

    @GetMapping("/users/{userId}/lists/latest")
    @ResponseBody
    public ShoppingList getShoppingList(String userId) {
        return latestList.get(userId);
    }
}
