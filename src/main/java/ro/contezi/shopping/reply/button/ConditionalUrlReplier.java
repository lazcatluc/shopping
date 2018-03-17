package ro.contezi.shopping.reply.button;

import ro.contezi.shopping.facebook.FacebookUrlButton;
import ro.contezi.shopping.facebook.MessageFromFacebook;

import java.util.List;

public interface ConditionalUrlReplier {
    boolean appliesUrl(MessageFromFacebook messageFromFacebook);
    List<FacebookUrlButton> urlReplies(MessageFromFacebook messageFromFacebook);
}
