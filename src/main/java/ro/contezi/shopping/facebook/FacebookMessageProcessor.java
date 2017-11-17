package ro.contezi.shopping.facebook;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import ro.contezi.shopping.reply.QuickReplyProvider;
import ro.contezi.shopping.reply.ReplyProvider;
import ro.contezi.shopping.reply.ReplySender;

public class FacebookMessageProcessor {

    private static final Logger LOGGER = Logger.getLogger(FacebookMessageProcessor.class);
    private final ReplyProvider replyProvider;
    private final ReplySender replySender;
    private final QuickReplyProvider quickReplyProvider;

    public FacebookMessageProcessor(ReplyProvider replyProvider, ReplySender replySender, QuickReplyProvider quickReplyProvider) {
        this.replyProvider = replyProvider;
        this.replySender = replySender;
        this.quickReplyProvider = quickReplyProvider;
    }

    @JmsListener(destination = "facebook", containerFactory = "myFactory")
    public void receiveMessage(MessageFromFacebook messageFromFacebook) {
        if (messageFromFacebook.getText() == null) {
            return;
        }
        LOGGER.info(messageFromFacebook);
        String reply = replyProvider.reply(messageFromFacebook);
        List<FacebookQuickReply> quickReplies = quickReplyProvider.reply(messageFromFacebook);
        FacebookMessage message = new FacebookMessage(reply, quickReplies.isEmpty() ? null : quickReplies, null);
        FacebookUser sender = messageFromFacebook.getSender();
        FacebookReply facebookReply = new FacebookReply(sender, message);
        replySender.send(facebookReply);
    }
}
