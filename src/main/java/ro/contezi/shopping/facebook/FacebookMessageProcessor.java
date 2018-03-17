package ro.contezi.shopping.facebook;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import ro.contezi.shopping.reply.button.UrlReplier;
import ro.contezi.shopping.reply.quick.QuickReplier;
import ro.contezi.shopping.reply.text.Replier;
import ro.contezi.shopping.reply.ReplySender;

import static org.slf4j.LoggerFactory.getLogger;

public class FacebookMessageProcessor {
    private static final Logger LOGGER = getLogger(FacebookMessageProcessor.class);
    private final Replier replier;
    private final ReplySender replySender;
    private final QuickReplier quickReplier;
    private final UrlReplier urlReplier;
    private final MessageLogger messageLogger;

    public FacebookMessageProcessor(Replier replier, ReplySender replySender,
                                    QuickReplier quickReplier, UrlReplier urlReplier, MessageLogger messageLogger) {
        this.replier = replier;
        this.replySender = replySender;
        this.quickReplier = quickReplier;
        this.urlReplier = urlReplier;
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
        FacebookMessage message;
        if (!quickReplies.isEmpty()) {
            message = new FacebookMessage(reply, quickReplies, null, null);
        } else {
            message = new FacebookMessage(reply);
        }
        FacebookUser sender = messageFromFacebook.getSender();
        FacebookReply facebookReply = new FacebookReply(sender, message);
        replySender.send(facebookReply);
        List<FacebookUrlButton> urlReplies = urlReplier.buttonReply(messageFromFacebook);
        if (!urlReplies.isEmpty()) {
            replySender.send(new FacebookReply(sender, new FacebookMessage(urlReplies)));
        }
    }
}
