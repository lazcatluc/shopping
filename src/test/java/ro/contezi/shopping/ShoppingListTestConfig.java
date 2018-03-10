package ro.contezi.shopping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.contezi.shopping.facebook.MessageLogger;
import ro.contezi.shopping.facebook.TargetMessages;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.reply.ReplySender;

@Configuration
@Import(Shopping.class)
public class ShoppingListTestConfig {

    @Value("${sleepAfterSendMessage:1000}")
    private int sleepAfterSendMessage;

    @Bean
    public ReplySender replySender(TargetMessages targetMessages) {
        return targetMessages::add;
    }

    @Bean
    public TargetMessages messages() {
        return new TargetMessages();
    }

    @Bean
    public MessageLogger messageLogger(TargetMessages targetMessages) {
        return messageFromFacebook ->
                messageFromFacebook.getEntry().forEach(facebookEntry ->
                        facebookEntry.getMessaging().forEach(targetMessages::add));
    }

    @Bean
    public Users whenUserActions(Webhook webhook) {
        return new Users(webhook, sleepAfterSendMessage);
    }

}