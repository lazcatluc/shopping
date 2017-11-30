package ro.contezi.shopping;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.jms.ConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
import ro.contezi.shopping.facebook.MessageLogger;
import ro.contezi.shopping.facebook.SignatureValidator;
import ro.contezi.shopping.facebook.Webhook;
import ro.contezi.shopping.author.AuthorJpaRepository;
import ro.contezi.shopping.author.AuthorRepository;
import ro.contezi.shopping.list.LatestList;
import ro.contezi.shopping.list.ShoppingListJpaRepository;
import ro.contezi.shopping.list.ShoppingListMessengerView;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.ShoppingListView;
import ro.contezi.shopping.list.action.AcceptShare;
import ro.contezi.shopping.list.action.BuyPartialMatches;
import ro.contezi.shopping.reply.quick.CompositeQuickReplier;
import ro.contezi.shopping.reply.text.CompositeReplier;
import ro.contezi.shopping.reply.FacebookReplySender;
import ro.contezi.shopping.list.action.InformOthers;
import ro.contezi.shopping.list.action.NewShoppingList;
import ro.contezi.shopping.reply.quick.QuickReplier;
import ro.contezi.shopping.list.action.RejectShare;
import ro.contezi.shopping.reply.text.Replier;
import ro.contezi.shopping.reply.ReplySender;
import ro.contezi.shopping.reply.text.Rose;
import ro.contezi.shopping.list.action.ShareList;
import ro.contezi.shopping.list.action.ShoppingListAdd;
import ro.contezi.shopping.list.action.ShoppingListBuy;
import ro.contezi.shopping.list.action.ShoppingListRemove;
import ro.contezi.shopping.reply.text.ShoppingListReplier;

@SpringBootApplication
@EnableJms
@EnableJpaRepositories
@EnableAsync
@EntityScan
public class Shopping {
    private static final Logger LOGGER = Logger.getLogger(Shopping.class);

    @Value("${facebook.secret}")
    private String facebookSecret;
    @Value("${facebook.graph.api.host}")
    private String graphApiHost;
    @Value("${facebook.graph.api.url}")
    private String graphApiUrl;
    @Value("${facebook.graph.api.version}")
    private String graphApiVersion;
    @Value("${facebook.token}")
    private String facebookToken;
    @Value("${rose.url}")
    private String roseUrl;
    @Value("${facebook.page.id}")
    private String pageId;
    @Value("${facebook.app.id}")
    private String appId;
    @Value("#{${users.default}}")
    private Map<String, String> user;
    @Value("#{${users.friend}}")
    private Map<String, String> friend;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ShoppingListJpaRepository shoppingListJpaRepository;
    @Autowired
    private AuthorJpaRepository authorJpaRepository;
    
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
    public Replier rose() throws URISyntaxException {
        return new Rose(roseUrl, restTemplate());
    }
    
    @Bean
    public AuthorRepository authorRepository() {
        return new AuthorRepository(authorJpaRepository, graphApi());
    }
    
    @Bean
    public GraphApi graphApi() {
        return new GraphApi(graphApiHost, graphApiVersion, facebookToken, restTemplate(),
                appId, facebookSecret, pageId);
    }

    @Bean
    public LatestList latestList() {
        return new LatestList(shoppingListRepository(), authorRepository());
    }
    
    @Bean
    public Replier replyProvider() throws URISyntaxException {
        return new CompositeReplier(rose(), authorRepository(), Arrays.asList(
            new ShoppingListAdd(shoppingListRepository(), shoppingListView(), latestList(), informOthers()),
            new ShoppingListRemove(shoppingListRepository(), shoppingListView(), latestList(), informOthers()),
            new ShoppingListBuy(shoppingListRepository(), shoppingListView(), latestList(), informOthers()),
            buyPartialMatches(),
            new ShoppingListReplier(shoppingListView(), latestList()),
            new ShareList(latestList(), authorRepository(), replySender()),
            new AcceptShare(shoppingListRepository(), authorRepository(), shoppingListView()),
            new RejectShare(),
            new NewShoppingList(shoppingListRepository(), shoppingListView(), authorRepository())
        ));
    }
    
    @Bean
    public QuickReplier quickReplyProvider() {
        return new CompositeQuickReplier(Collections.singletonList(
                buyPartialMatches()
        ));
    }

    @Bean
    public BuyPartialMatches buyPartialMatches() {
        return new BuyPartialMatches(shoppingListRepository(), shoppingListView(), latestList(), informOthers());
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
    public MessageLogger messageLogger() {
        return LOGGER::info;
    }
    
    @Bean
    public FacebookMessageProcessor facebookMessageProcessor() throws URISyntaxException {
        return new FacebookMessageProcessor(replyProvider(), replySender(),
                quickReplyProvider(), messageLogger());
    }

    @Bean
    public InformOthers informOthers() {
        return new InformOthers(replySender());
    }

    @Bean
    public Webhook webhook() throws InvalidKeyException, NoSuchAlgorithmException {
        return new Webhook(signatureValidator(), jmsTemplate, pageId);
    }

    @Bean
    @Primary
    public ConfigurableUser defaultUser() {
        return new ConfigurableUser(user);
    }

    @Bean
    public ConfigurableUser friend() {
        return new ConfigurableUser(friend);
    }

    public static void main(String[] args) {
        SpringApplication.run(Shopping.class, args);
    }
}
