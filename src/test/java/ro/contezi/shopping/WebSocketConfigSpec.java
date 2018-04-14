package ro.contezi.shopping;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ro.contezi.shopping.list.ShoppingList;
import ro.contezi.shopping.list.ShoppingListRepository;
import ro.contezi.shopping.list.action.item.ShoppingItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingListTestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("default")
public class WebSocketConfigSpec {

    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;
    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    @Autowired
    @Qualifier("shoppingListRepository")
    private ShoppingListRepository shoppingListRepository;

    @Before
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.stompClient = new WebSocketStompClient(new SockJsClient(transports));
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void getGreeting() throws Exception {
        ShoppingList shoppingList = new ShoppingList();
        shoppingListRepository.saveShoppingList(shoppingList);
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<RuntimeException> failure = new AtomicReference<>();

        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/topic/items/" + shoppingList.getId(), new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ShoppingItem.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        ShoppingItem hello = (ShoppingItem) payload;
                        try {
                            assertThat(hello.getItemName()).isEqualTo("hello");
                        } catch (RuntimeException t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });
                try {
                    session.send("/ws/item", new ShoppingItem("1", shoppingList.getId(), "hello", false, false, 1.0));
                } catch (RuntimeException t) {
                    failure.set(t);
                    latch.countDown();
                }
            }
        };

        this.stompClient.connect("ws://localhost:{port}/list", this.headers, handler, this.port);

        if (latch.await(3, TimeUnit.SECONDS)) {
            RuntimeException runtimeException = failure.get();
            if (runtimeException != null) {
                throw runtimeException;
            }
        }
        else {
            fail("Greeting not received");
        }

    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<RuntimeException> failure;

        TestSessionHandler(AtomicReference<RuntimeException> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new IllegalArgumentException(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(new IllegalStateException(ex));
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(new IllegalStateException(ex));
        }
    }
}