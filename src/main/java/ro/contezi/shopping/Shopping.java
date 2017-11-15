package ro.contezi.shopping;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import ro.contezi.shopping.facebook.FacebookMessageProcessor;
import ro.contezi.shopping.facebook.FacebookWebhookSignatureValidator;
import ro.contezi.shopping.facebook.SignatureValidator;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.list.ShoppingListJpaRepository;
import ro.contezi.shopping.list.ShoppingListMessengerView;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.reply.CompositeReplyProvider;
import ro.contezi.shopping.reply.FacebookReplySender;
import ro.contezi.shopping.reply.NewShoppingList;
import ro.contezi.shopping.reply.ReplyProvider;
import ro.contezi.shopping.reply.ReplySender;
import ro.contezi.shopping.reply.Rose;
import ro.contezi.shopping.reply.ShoppingListAdd;
import ro.contezi.shopping.reply.ShoppingListBuy;
import ro.contezi.shopping.reply.ShoppingListRemove;
import ro.contezi.shopping.reply.ShoppingListReplyProvider;

@SpringBootApplication
@EnableJms
@EnableJpaRepositories
@EnableAsync
@EntityScan
public class Shopping {
    
    @Value("${facebook.secret}")
    private String facebookSecret;
    @Value("${facebook.graph.api.url}")
    private String graphApiUrl;
    @Value("${facebook.token}")
    private String facebookToken;
    @Value("${rose.url}")
    private String roseUrl;
    @Value("${facebook.page.id}")
    private String pageId;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ShoppingListJpaRepository shoppingListJpaRepository;
    
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    
    @Bean
    public SignatureValidator signatureValidator() throws InvalidKeyException, NoSuchAlgorithmException {
        return new FacebookWebhookSignatureValidator(facebookSecret);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public ReplyProvider rose() throws URISyntaxException {
        return new Rose(roseUrl, restTemplate());
    }
    
    @Bean
    public ReplyProvider replyProvider() throws URISyntaxException {
        return new CompositeReplyProvider(rose(), Arrays.asList(
                new ShoppingListAdd(shoppingListRepository(), shoppingListView()),
                new ShoppingListRemove(shoppingListRepository(), shoppingListView()),
                new ShoppingListBuy(shoppingListRepository(), shoppingListView()),
                new ShoppingListReplyProvider(shoppingListRepository(), shoppingListView()),
                new NewShoppingList(shoppingListRepository(), shoppingListView())
            ));
    }
    
    @Bean
    public ShoppingListView shoppingListView() {
        return new ShoppingListMessengerView();
    }

    @Bean
    public ShoppingListRepository shoppingListRepository() {
        return shoppingListJpaRepository;
    }

    @Bean
    public ReplySender replySender() {
        return new FacebookReplySender(restTemplate(), facebookToken, graphApiUrl);
    }
    
    @Bean
    public FacebookMessageProcessor facebookMessageProcessor() throws URISyntaxException {
        return new FacebookMessageProcessor(replyProvider(), replySender());
    }
    
    @Bean
    public Webhook webhook() throws InvalidKeyException, NoSuchAlgorithmException {
        return new Webhook(signatureValidator(), jmsTemplate, pageId);
    }

    public static void main(String[] args) {
        SpringApplication.run(Shopping.class, args);
    }
}
