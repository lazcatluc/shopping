package ro.contezi.shopping.reply.text;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public interface Replier {

    String reply(MessageFromFacebook messageFromFacebook);

}