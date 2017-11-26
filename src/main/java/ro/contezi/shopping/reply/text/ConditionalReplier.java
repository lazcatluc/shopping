package ro.contezi.shopping.reply.text;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface ConditionalReplier extends Replier {
    boolean applies(MessageFromFacebook messageFromFacebook);
}
