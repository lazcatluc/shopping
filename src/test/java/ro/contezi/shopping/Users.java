package ro.contezi.shopping;

import java.io.IOException;
import java.io.UncheckedIOException;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.list.ShoppingList;

public class Users {
    private final Webhook webhook;

    public Users(Webhook webhook) {
        this.webhook = webhook;
    }

    public class Action {
        private final String user;
        private final int sleepAfterSendMessage = 1000;

        private Action(String user) {
            this.user = user;
        }

        private void buildMessage(Messages builder) {
            try {
                webhook.receiveMessage(builder.sender(user).build());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            try {
                Thread.sleep(sleepAfterSendMessage);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public Action says(String simpleMessage) {
            buildMessage(new Messages().text(simpleMessage));
            return this;
        }

        public Action adds(String ingredient) {
            return says("add " + ingredient);
        }

        public Action removes(String ingredient) {
            return says("remove " + ingredient);
        }

        public Action buys(String ingredient) {
            return says("buy " + ingredient);
        }

        public Action displaysCurrentList() {
            return says("list");
        }

        public Action startsANewList() {
            return says("new");
        }

        public Action sharesListWith(String anotherUser) {
            return says("share with " + anotherUser);
        }

        public Action clicksOnQuickReply(String quickReplyPaylod) {
            buildMessage(new Messages().quickReplyPayload(quickReplyPaylod));
            return this;
        }

        public Action acceptsSharedList(ShoppingList shoppingList) {
            return clicksOnQuickReply("accept_share " + shoppingList.getId());
        }

        public Action rejectsSharedList(ShoppingList shoppingList) {
            return clicksOnQuickReply("reject_share " + shoppingList.getId());
        }

        @Override
        public String toString() {
            return user;
        }

    }

    public Action user(String user) {
        return new Action(user);
    }
}
