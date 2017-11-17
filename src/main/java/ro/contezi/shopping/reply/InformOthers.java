package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.FacebookReply;
import ro.contezi.shopping.facebook.FacebookUser;
import ro.contezi.shopping.list.Author;
import ro.contezi.shopping.list.ShoppingList;

public class InformOthers {
    private final ReplySender replySender;

    public InformOthers(ReplySender replySender) {
        this.replySender = replySender;
    }

    public void informOthers(FacebookUser sender, ShoppingList shoppingList, String message) {
        FacebookMessage facebookMessage = new FacebookMessage(message, null, null);
        shoppingList.getAllInterestedParties().stream().filter(party -> !sender.getId().equals(party.getId()))
                .map(Author::getId).map(FacebookUser::new).forEach(user -> replySender.send(new FacebookReply(user, facebookMessage)));
    }
}
