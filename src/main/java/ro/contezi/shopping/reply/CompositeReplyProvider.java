package ro.contezi.shopping.reply;

import java.util.List;

import ro.contezi.shopping.facebook.MessageFromFacebook;

public class CompositeReplyProvider implements ReplyProvider {
    private final ReplyProvider defaultReply;
    private final List<ConditionalReplyProvider> conditionalReplies;
    
    public CompositeReplyProvider(ReplyProvider defaultReply, List<ConditionalReplyProvider> conditionalReplies) {
        this.defaultReply = defaultReply;
        this.conditionalReplies = conditionalReplies;
    }

    @Override
    public String reply(MessageFromFacebook message) {
        return conditionalReplies.stream().filter(replyProvider -> replyProvider.applies(message))
            .findFirst().map(replyProvider -> replyProvider.reply(message))
            .orElseGet(() -> defaultReply.reply(message));
    }
}
