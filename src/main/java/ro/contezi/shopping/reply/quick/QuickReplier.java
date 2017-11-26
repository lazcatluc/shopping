package ro.contezi.shopping.reply.quick;

import java.util.List;

import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface QuickReplier {
    List<FacebookQuickReply> quickReply(MessageFromFacebook messageFromFacebook);
}
