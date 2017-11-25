package ro.contezi.shopping.reply;

import java.util.Collections;
import java.util.List;

import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;

public class CompositeQuickReplyProvider implements QuickReplyProvider {
    
    private final List<ConditionalQuickReplyProvider> conditionalReplies;
        
    public CompositeQuickReplyProvider(List<ConditionalQuickReplyProvider> conditionalReplies) {
        this.conditionalReplies = conditionalReplies;
    }

    @Override
    public List<FacebookQuickReply> quickReply(MessageFromFacebook message) {
        return conditionalReplies.stream().filter(replyProvider -> replyProvider.appliesQuickReply(message))
                .findFirst().map(replyProvider -> replyProvider.quickReplies(message))
                .orElse(Collections.emptyList());
    }

}
