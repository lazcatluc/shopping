package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface ReplyProvider {

    String reply(MessageFromFacebook messageFromFacebook);

}