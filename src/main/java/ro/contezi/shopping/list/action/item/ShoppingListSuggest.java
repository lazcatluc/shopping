package ro.contezi.shopping.list.action.item;

import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.author.AuthorRepository;
import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.reply.text.ConditionalReplier;

import java.util.List;
import java.util.Optional;

public class ShoppingListSuggest implements ConditionalReplier {
    private final ShoppingListAdd shoppingListAdd;
    private final ItemSuggestions itemSuggestions;
    private final AuthorRepository authorRepository;
    private final LatestList latestList;

    public ShoppingListSuggest(ShoppingListAdd shoppingListAdd, ItemSuggestions itemSuggestions,
                               AuthorRepository authorRepository, LatestList latestList) {
        this.shoppingListAdd = shoppingListAdd;
        this.itemSuggestions = itemSuggestions;
        this.authorRepository = authorRepository;
        this.latestList = latestList;
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return Optional.ofNullable(messageFromFacebook.getText()).map(FacebookMessage::getText)
                .orElse("").trim().toLowerCase().equals("suggest");
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        String authorId = messageFromFacebook.getSender().getId();
        Author author = authorRepository.getInitializedAuthor(authorId);
        ShoppingList shoppingList = latestList.get(authorId);
        List<String> suggestions = itemSuggestions.getSuggestions(author);
        String reply = "Oops, sorry, I have no idea what you might want. " +
                "Use the shopping list some more so I have some idea";
        for (String suggestion : suggestions) {
            reply = shoppingListAdd.buildReplies(shoppingList, suggestion);
        }
        return reply;
    }
}
