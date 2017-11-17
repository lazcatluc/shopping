package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.AuthorRepository;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class AcceptShare implements ConditionalReplyProvider {

    private final ShoppingListRepository shoppingListRepository;
    private final AuthorRepository authorRepository;
    private final ShoppingListView shoppingListView;
    
    public AcceptShare(ShoppingListRepository shoppingListRepository, AuthorRepository authorRepository,
            ShoppingListView shoppingListView) {
        this.shoppingListRepository = shoppingListRepository;
        this.authorRepository = authorRepository;
        this.shoppingListView = shoppingListView;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        String shoppingListId = messageFromFacebook.getText().getText().substring("accept_share ".length());
        ShoppingList shared = shoppingListRepository.share(shoppingListId, authorRepository.getInitializedAuthor(messageFromFacebook.getSender().getId()));
        return shoppingListView.displayShoppingList(shared.getItems());
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().startsWith("accept_share ");
    }

}
