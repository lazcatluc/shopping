package ro.contezi.shopping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.contezi.shopping.facebook.MessageLogger;
import ro.contezi.shopping.facebook.TargetedMessage;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.reply.ReplySender;

@Configuration
@Import(Shopping.class)
public class ShoppingListTestConfig {

    private List<TargetedMessage> messages = new ArrayList<>();

    @Value("${sleepAfterSendMessage:1000}")
    private int sleepAfterSendMessage;

    @Bean
    public ReplySender replySender() {
        return messages::add;
    }

    @Bean
    public List<TargetedMessage> messages() {
        return messages;
    }

    @Bean
    public MessageLogger messageLogger() {
        return messageFromFacebook ->
                messageFromFacebook.getEntry().forEach(facebookEntry ->
                        facebookEntry.getMessaging().forEach(messages::add));
    }

    @Bean
    public Users whenUserActions(Webhook webhook) {
        return new Users(webhook, sleepAfterSendMessage);
    }

}