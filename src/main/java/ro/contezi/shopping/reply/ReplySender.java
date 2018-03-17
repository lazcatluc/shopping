package ro.contezi.shopping.reply;

import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.FacebookReply;
import ro.contezi.shopping.facebook.FacebookUser;

public interface ReplySender {
    void send(FacebookReply reply);
    default void send(String senderId, String message) {
        send(new FacebookReply(new FacebookUser(senderId),
                new FacebookMessage(message, null, null, null)));
    }
}
