package ro.contezi.shopping.list.action.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.contezi.shopping.facebook.FacebookQuickReply;
import ro.contezi.shopping.facebook.MessageFromFacebook;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListItem;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.list.action.InformOthers;
import ro.contezi.shopping.list.action.item.ShoppingListBuy;
import ro.contezi.shopping.reply.quick.ConditionalQuickReplier;

public class BuyPartialMatches extends ShoppingListBuy implements ConditionalQuickReplier {
    public BuyPartialMatches(ShoppingListRepository shoppingListRepository, ShoppingListView shoppingListView,
                             LatestList latestList, InformOthers informOthers,
                             SimpMessagingTemplate simpMessagingTemplate) {
        super(shoppingListRepository, shoppingListView, latestList, informOthers, simpMessagingTemplate);
    }

    @Override
    public List<FacebookQuickReply> quickReplies(MessageFromFacebook messageFromFacebook) {
        ShoppingList shoppingList = getLatestList().get(messageFromFacebook.getSender().getId());
        Set<String> matches = new HashSet<>(getPartialMatches(shoppingList,
                getRemainingText(messageFromFacebook)));
        matches.add(getRemainingText(messageFromFacebook));
        FacebookQuickReply.Builder builder = new FacebookQuickReply.Builder();
        return matches.stream().map(match ->
                builder.withTitle(getCheckedTitle(shoppingList, match))
                       .withPayload("buy " + match).build()).collect(Collectors.toList());
    }

    private String getCheckedTitle(ShoppingList shoppingList, String match) {
        return shoppingList.getItems().stream().filter(shoppingListItem -> shoppingListItem.getItemName()
            .equalsIgnoreCase(match)).filter(shoppingListItem -> shoppingListItem.getBoughtDate() != null)
                .findAny().map(ShoppingListItem::getItemName).map(title -> title + " ✔").orElse(match);
    }

    @Override
    public boolean appliesQuickReply(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getText().getText().toLowerCase().startsWith(actionDescription()) &&
               !super.appliesToRemainingText(getLatestList().get(messageFromFacebook.getSender().getId()),
                    getRemainingText(messageFromFacebook));
    }

    @Override
    protected boolean appliesToRemainingText(ShoppingList shoppingList, String text) {
        return !super.appliesToRemainingText(shoppingList, text);
    }

    @Override
    public String reply(MessageFromFacebook messageFromFacebook) {
        return "Which one of these?";
    }
}
