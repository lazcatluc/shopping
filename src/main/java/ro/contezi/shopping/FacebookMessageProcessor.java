package ro.contezi.shopping;

import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;

import ro.contezi.shopping.model.FacebookMessage;
import ro.contezi.shopping.model.FacebookReply;
import ro.contezi.shopping.model.FacebookUser;
import ro.contezi.shopping.model.MessageFromFacebook;

public class FacebookMessageProcessor {

    private static final Logger LOGGER = Logger.getLogger(FacebookMessageProcessor.class);
    private final ReplyProvider replyProvider;
    private final ReplySender replySender;

    public FacebookMessageProcessor(ReplyProvider replyProvider, ReplySender replySender) {
        this.replyProvider = replyProvider;
        this.replySender = replySender;
    }

    @JmsListener(destination = "facebook", containerFactory = "myFactory")
    public void receiveMessage(MessageFromFacebook messageFromFacebook) {
        if (messageFromFacebook.getText() == null) {
            return;
        }
        LOGGER.info(messageFromFacebook);
        String reply = replyProvider.reply(messageFromFacebook);
        FacebookMessage message = new FacebookMessage(reply);
        FacebookUser sender = messageFromFacebook.getSender();
        FacebookReply facebookReply = new FacebookReply(sender, message);
        replySender.send(facebookReply);
    }
}
