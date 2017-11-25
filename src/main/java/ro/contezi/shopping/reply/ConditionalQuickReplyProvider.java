package ro.contezi.shopping.reply;

import java.util.List;

import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface ConditionalQuickReplyProvider {
    boolean appliesQuickReply(MessageFromFacebook messageFromFacebook);
    List<FacebookQuickReply> quickReplies(MessageFromFacebook messageFromFacebook);
}
