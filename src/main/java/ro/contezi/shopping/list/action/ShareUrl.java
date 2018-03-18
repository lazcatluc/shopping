package ro.contezi.shopping.list.action;

import ro.contezi.shopping.facebook.FacebookUrlButton;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.reply.button.ConditionalUrlReplier;
import ro.contezi.shopping.reply.text.ConditionalReplier;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ShareUrl implements ConditionalUrlReplier {

    private final Predicate<MessageFromFacebook> acceptShare;
    private final String applicationUrl;
    private final LatestList latestList;

    public ShareUrl(ConditionalReplier acceptShare, String applicationUrl, LatestList latestList) {
        this(acceptShare::applies, applicationUrl, latestList);
    }

    public ShareUrl(Predicate<MessageFromFacebook> acceptShare, String applicationUrl, LatestList latestList) {
        this.acceptShare = acceptShare;
        this.applicationUrl = applicationUrl;
        this.latestList = latestList;
    }

    @Override
    public boolean appliesUrl(MessageFromFacebook messageFromFacebook) {
        return acceptShare.test(messageFromFacebook);
    }

    @Override
    public List<FacebookUrlButton> urlReplies(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = latestList.get(messageFromFacebook.getSender().getId());
        FacebookUrlButton facebookUrlButton = new FacebookUrlButton();
        facebookUrlButton.setTitle("Open List");
        facebookUrlButton.setUrl(applicationUrl + "/index.html#" + shoppingList.getId());
        return Collections.singletonList(facebookUrlButton);
    }
}
