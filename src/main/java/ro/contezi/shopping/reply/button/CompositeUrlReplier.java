package ro.contezi.shopping.reply.button;

import ro.contezi.shopping.facebook.FacebookUrlButton;
import ro.contezi.shopping.facebook.MessageFromFacebook;

import java.util.Collections;
import java.util.List;

public class CompositeUrlReplier implements UrlReplier {
    private final List<ConditionalUrlReplier> conditionalReplies;

    public CompositeUrlReplier(List<ConditionalUrlReplier> conditionalReplies) {
        this.conditionalReplies = conditionalReplies;
    }

    @Override
    public List<FacebookUrlButton> buttonReply(MessageFromFacebook messageFromFacebook) {
        return conditionalReplies.stream().filter(replyProvider -> replyProvider.appliesUrl(messageFromFacebook))
                .findFirst().map(replyProvider -> replyProvider.urlReplies(messageFromFacebook))
                .orElse(Collections.emptyList());
    }
}
