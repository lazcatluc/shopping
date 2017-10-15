package ro.contezi.shopping;

import ro.contezi.shopping.model.MessageFromFacebook;

public interface ReplyProvider {

    String reply(MessageFromFacebook messageFromFacebook);

}