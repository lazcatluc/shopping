package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface ConditionalReplyProvider extends ReplyProvider {
    boolean applies(MessageFromFacebook messageFromFacebook);
}
