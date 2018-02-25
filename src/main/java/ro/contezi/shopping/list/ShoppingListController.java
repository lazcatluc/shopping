package ro.contezi.shopping.list;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.contezi.shopping.author.AuthorJpaRepository;

@RequestMapping("/users")
public class ShoppingListController {
    private final LatestList latestList;
    private final AuthorJpaRepository authorRepository;

    public ShoppingListController(LatestList latestList, AuthorJpaRepository authorRepository) {
        this.latestList = latestList;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/{userId}/lists/latest")
    @ResponseBody
    public ResponseEntity<ShoppingList> getShoppingList(@PathVariable String userId) {
        if (authorRepository.findOne(userId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestList.get(userId));
    }
}
