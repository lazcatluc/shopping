package ro.contezi.shopping.facebook;

public interface TargetedMessage {
    FacebookUser getRecipient();

    FacebookMessage getMessage();

    default FacebookUser getSender() {
        return FacebookUser.PAGE_USER;
    }
}
