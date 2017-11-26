package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface Replier {

    String reply(MessageFromFacebook messageFromFacebook);

}