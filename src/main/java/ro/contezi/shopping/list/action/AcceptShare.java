package ro.contezi.shopping.list.action;

import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.FacebookReply;
import ro.contezi.shopping.facebook.FacebookUser;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.author.AuthorRepository;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.reply.ReplySender;
import ro.contezi.shopping.reply.text.ConditionalReplier;

public class AcceptShare implements ConditionalReplier {

    private final ShoppingListRepository shoppingListRepository;
    private final AuthorRepository authorRepository;
    private final ShoppingListView shoppingListView;
    private final ReplySender replySender;

    public AcceptShare(ShoppingListRepository shoppingListRepository,
                       AuthorRepository authorRepository,
                       ShoppingListView shoppingListView, ReplySender replySender) {
        this.shoppingListRepository = shoppingListRepository;
        this.authorRepository = authorRepository;
        this.shoppingListView = shoppingListView;
        this.replySender = replySender;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        String shoppingListId = messageFromFacebook.getText().getQuickReply()
                .getPayload().substring("accept_share ".length());
        Author initializedAuthor = authorRepository.getInitializedAuthor(
                messageFromFacebook.getSender().getId());
        ShoppingList shared = shoppingListRepository.share(shoppingListId, initializedAuthor);
        replySender.send(shared.getAuthor().getId(), initializedAuthor.getFirstName() +
                " has accepted to share.");
        return shoppingListView.displayShoppingList(shared.getItems());
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getQuickReply() != null &&
                messageFromFacebook.getText().getQuickReply().getPayload().startsWith("accept_share ");
    }

}
