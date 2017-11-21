package ro.contezi.shopping.facebook;

import java.util.List;
import org.springframework.jms.annotation.JmsListener;
import ro.contezi.shopping.reply.QuickReplyProvider;
import ro.contezi.shopping.reply.ReplyProvider;
import ro.contezi.shopping.reply.ReplySender;

public class FacebookMessageProcessor {

    private final ReplyProvider replyProvider;
    private final ReplySender replySender;
    private final QuickReplyProvider quickReplyProvider;
    private final MessageLogger messageLogger;

    public FacebookMessageProcessor(ReplyProvider replyProvider, ReplySender replySender,
                                    QuickReplyProvider quickReplyProvider, MessageLogger messageLogger) {
        this.replyProvider = replyProvider;
        this.replySender = replySender;
        this.quickReplyProvider = quickReplyProvider;
        this.messageLogger = messageLogger;
    }

    @JmsListener(destination = "facebook", containerFactory = "myFactory")
    public void receiveMessage(MessageFromFacebook messageFromFacebook) {
        if (messageFromFacebook.getText() == null) {
            return;
        }
        messageLogger.logMessage(messageFromFacebook);
        String reply = replyProvider.reply(messageFromFacebook);
        List<FacebookQuickReply> quickReplies = quickReplyProvider.reply(messageFromFacebook);
        FacebookMessage message = new FacebookMessage(reply,
                quickReplies.isEmpty() ? null : quickReplies, null);
        FacebookUser sender = messageFromFacebook.getSender();
        FacebookReply facebookReply = new FacebookReply(sender, message);
        replySender.send(facebookReply);
    }

}
