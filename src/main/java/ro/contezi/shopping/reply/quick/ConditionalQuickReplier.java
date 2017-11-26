package ro.contezi.shopping.reply.quick;

import java.util.List;

import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface ConditionalQuickReplier {
    boolean appliesQuickReply(MessageFromFacebook messageFromFacebook);
    List<FacebookQuickReply> quickReplies(MessageFromFacebook messageFromFacebook);
}
