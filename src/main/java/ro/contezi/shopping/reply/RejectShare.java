package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public class RejectShare implements ConditionalReplyProvider {

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        return "Ok, no problem";
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().startsWith("reject_share ");
    }

}
