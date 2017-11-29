package ro.contezi.shopping;

public class Messages {
    private String sender = "";
    private String text = "OK";
    private String quickReplyPayload = "";

    public String build() {
        return "{\"object\":\"page\",\"entry\":[{\"id\":\"535405013470180\",\"time\":1510896979779,\"messaging\":["
                + "{\"sender\":{\"id\":\"" + sender + "\"},\"recipient\":{\"id\":\"535405013470180\"},\"timestamp\":1510896978831,"
                + "\"message\":{" + (quickReplyPayload.isEmpty() ? "" : "\"quick_reply\":{\"payload\":\"" + quickReplyPayload + "\"},")
                + "\"mid\":\"mid.$cAAHm8o_kpEtlUCpQolfHuUHC30ja\",\"seq\":1171418,\"text\":\"" + text + "\"}}]}]}";
    }

    public Messages sender(String sender) {
        this.sender = sender;
        return this;
    }

    public Messages quickReplyPayload(String quickReplyPayload) {
        this.quickReplyPayload = quickReplyPayload;
        this.text = "OK";
        return this;
    }

    public Messages text(String text) {
        this.text = text;
        this.quickReplyPayload = "";
        return this;
    }
}
