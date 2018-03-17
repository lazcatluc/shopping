package ro.contezi.shopping.reply.button;

import ro.contezi.shopping.facebook.FacebookUrlButton;
import ro.contezi.shopping.facebook.MessageFromFacebook;

import java.util.List;

public interface UrlReplier {
    List<FacebookUrlButton> buttonReply(MessageFromFacebook messageFromFacebook);
}
