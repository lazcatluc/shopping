package ro.contezi.shopping.reply;

import ro.contezi.shopping.ReplyProvider;
import ro.contezi.shopping.model.MessageFromFacebook;

public interface ConditionalReplyProvider extends ReplyProvider {
    boolean applies(MessageFromFacebook messageFromFacebook);
}
