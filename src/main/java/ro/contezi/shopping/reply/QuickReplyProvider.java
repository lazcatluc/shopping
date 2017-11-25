package ro.contezi.shopping.reply;

import java.util.List;

import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface QuickReplyProvider {
    List<FacebookQuickReply> quickReply(MessageFromFacebook messageFromFacebook);
}
