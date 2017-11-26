package ro.contezi.shopping.facebook;

import java.util.List;
import org.springframework.jms.annotation.JmsListener;
import ro.contezi.shopping.reply.quick.QuickReplier;
import ro.contezi.shopping.reply.text.Replier;
import ro.contezi.shopping.reply.ReplySender;

public class FacebookMessageProcessor {

    private final Replier replier;
    private final ReplySender replySender;
    private final QuickReplier quickReplier;
    private final MessageLogger messageLogger;

    public FacebookMessageProcessor(Replier replier, ReplySender replySender,
                                    QuickReplier quickReplier, MessageLogger messageLogger) {
        this.replier = replier;
        this.replySender = replySender;
        this.quickReplier = quickReplier;
        this.messageLogger = messageLogger;
    }

    @JmsListener(destination = "facebook", containerFactory = "myFactory")
    public void receiveMessage(MessageFromFacebook messageFromFacebook) {
        if (messageFromFacebook.getText() == null) {
            return;
        }
        messageLogger.logMessage(messageFromFacebook);
        String reply = replier.reply(messageFromFacebook);
        List<FacebookQuickReply> quickReplies = quickReplier.quickReply(messageFromFacebook);
        FacebookMessage message = new FacebookMessage(reply,
                quickReplies.isEmpty() ? null : quickReplies, null);
        FacebookUser sender = messageFromFacebook.getSender();
        FacebookReply facebookReply = new FacebookReply(sender, message);
        replySender.send(facebookReply);
    }

}
