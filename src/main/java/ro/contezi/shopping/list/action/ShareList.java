package ro.contezi.shopping.list.action;

import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import ro.contezi.shopping.facebook.FacebookMessage;
import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.FacebookReply;
import ro.contezi.shopping.facebook.FacebookUser;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.author.AuthorRepository;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.reply.ReplySender;
import ro.contezi.shopping.reply.text.ConditionalReplier;

public class ShareList implements ConditionalReplier {

    private static final Logger LOGGER = Logger.getLogger(ShareList.class);
    private static final String SHARE_WITH = "share with ";

    private final LatestList latestList;
    private final AuthorRepository authorRepository;
    private final ReplySender replySender;

    public ShareList(LatestList latestList, AuthorRepository authorRepository, ReplySender replySender) {
        this.latestList = latestList;
        this.authorRepository = authorRepository;
        this.replySender = replySender;
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = latestList.get(messageFromFacebook.getSender().getId());
        String name = messageFromFacebook.getText().getText().substring(SHARE_WITH.length());
        List<Author> potentialAuthors;
        if (name.contains(" ")) {
            String firstName = name.substring(0, name.indexOf(' '));
            String lastName = name.substring(name.indexOf(' ') + 1);
            potentialAuthors = authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
        }
        else {
            potentialAuthors = authorRepository.findByFirstNameIgnoreCase(name);
        }
        if (potentialAuthors.isEmpty()) {
            return "Sorry, I don't know anyone by that name";
        }
        if (potentialAuthors.size() > 1) {
            return "I have many by that name";
        }
        if (shoppingList.isSharedWith(potentialAuthors.get(0))) {
            return "You are already sharing with them!";
        }
        FacebookReply replyToShare = new FacebookReply(new FacebookUser(potentialAuthors.get(0).getId()), 
                new FacebookMessage(shoppingList.getAuthor().getFirstName()+" "+shoppingList.getAuthor().getLastName() + " would like to share the shopping list with you!", 
                        Arrays.asList(new FacebookQuickReply.Builder().withTitle("OK").withPayload("accept_share "+shoppingList.getId()).build(),
                                      new FacebookQuickReply.Builder().withTitle("No, thanks").withPayload("reject_share "+shoppingList.getId()).build()
                        ), null));
        replySender.send(replyToShare);
        return "Ok, I've asked " + name + " if they want to share!";
    }

    @Override
    public boolean applies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith(SHARE_WITH);
    }

}
