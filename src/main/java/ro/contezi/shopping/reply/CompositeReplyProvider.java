package ro.contezi.shopping.reply;

import java.util.List;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.AuthorRepository;

public class CompositeReplyProvider implements ReplyProvider {
    private final ReplyProvider defaultReply;
    private final AuthorRepository authorRepository;
    private final List<ConditionalReplyProvider> conditionalReplies;
    
    public CompositeReplyProvider(ReplyProvider defaultReply, AuthorRepository authorRepository, List<ConditionalReplyProvider> conditionalReplies) {
        this.defaultReply = defaultReply;
        this.authorRepository = authorRepository;
        this.conditionalReplies = conditionalReplies;
    }

    @Override
    public String reply(MessageFromFacebook message) {
        authorRepository.getInitializedAuthor(message.getSender().getId());
        return conditionalReplies.stream().filter(replyProvider -> replyProvider.applies(message))
            .findFirst().map(replyProvider -> replyProvider.reply(message))
            .orElseGet(() -> defaultReply.reply(message));
    }
}
