package ro.contezi.shopping.list.action;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.reply.text.ConditionalReplier;

public class RejectShare implements ConditionalReplier {

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        return "Ok, no problem";
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getQuickReply() != null &&
                messageFromFacebook.getText().getQuickReply().getPayload().startsWith("reject_share ");
    }

}
