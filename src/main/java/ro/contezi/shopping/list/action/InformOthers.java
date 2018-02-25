package ro.contezi.shopping.list.action;

import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.facebook.FacebookUser;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.reply.ReplySender;

public class InformOthers {
    private final ReplySender replySender;

    public InformOthers(ReplySender replySender) {
        this.replySender = replySender;
    }

    public void informOthers(String senderId, ShoppingList shoppingList, String message) {
        shoppingList.getAllInterestedParties().stream().filter(party -> !senderId.equals(party.getId()))
                .map(Author::getId).forEach(user -> replySender.send(user, message));
    }
}
