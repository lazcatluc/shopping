package ro.contezi.shopping.list.action.item;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.contezi.shopping.author.AuthorRepository;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListItem;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.list.action.InformOthers;

public class ShoppingListRemove extends ShoppingListAction {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SuggestionRemovalRepository suggestionRemovalRepository;
    private final AuthorRepository authorRepository;

    public ShoppingListRemove(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                              LatestList latestList, InformOthers informOthers,
                              SimpMessagingTemplate simpMessagingTemplate,
                              SuggestionRemovalRepository suggestionRemovalRepository,
                              AuthorRepository authorRepository) {
        super(latestList, shoppingListView, informOthers, shoppingListRepository);
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.suggestionRemovalRepository = suggestionRemovalRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void executeAction(String shoppingListId, String item, String sender) {
        ShoppingListItem shoppingListItem = getShoppingListRepository().remove(shoppingListId, item);
        if (shoppingListItem.getSuggested()) {
            suggestionRemovalRepository.removeItem(authorRepository.getInitializedAuthor(sender), item);
        }
        ShoppingItem shoppingItem = new ShoppingItem(shoppingListItem, true);
        simpMessagingTemplate.convertAndSend("/topic/items/"+shoppingListId, shoppingItem);
    }

    @Override
    protected String actionDescription() {
        return "remove";
    }

}
