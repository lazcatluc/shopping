package ro.contezi.shopping.reply;

import java.util.List;

import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.author.AuthorRepository;

public class CompositeReplier implements Replier {
    private final Replier defaultReply;
    private final AuthorRepository authorRepository;
    private final List<ConditionalReplier> conditionalReplies;
    
    public CompositeReplier(Replier defaultReply, AuthorRepository authorRepository, List<ConditionalReplier> conditionalReplies) {
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
