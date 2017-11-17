package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.AuthorRepository;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;

public class NewShoppingList implements ConditionalReplyProvider {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListView shoppingListView;
    private final AuthorRepository authorRepository;
    
    public NewShoppingList(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView, AuthorRepository authorRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListView = shoppingListView;
        this.authorRepository = authorRepository;
    }
    
    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setAuthor(authorRepository.getInitializedAuthor(messageFromFacebook.getSender().getId()));

        return shoppingListView.displayShoppingList(shoppingListRepository.saveShoppingList(shoppingList).getItems());
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().equals("new");
    }

}
