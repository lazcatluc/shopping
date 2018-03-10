package ro.contezi.shopping.facebook;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

public class TargetMessages {
    private static final Logger LOGGER = getLogger(TargetMessages.class);
    private final List<TargetedMessage> messages = new ArrayList<>();

    public synchronized boolean add(TargetedMessage message) {
        LOGGER.info("Adding message {}", message);
        boolean add = messages.add(message);
        LOGGER.info("{} messages", messages.size());
        return add;
    }

    public synchronized void clear() {
        LOGGER.info("Clearing messages");
        messages.clear();
        LOGGER.info("{} messages", messages.size());
    }

    public synchronized FacebookMessage lastMessage() {
        LOGGER.info("Last message of {}", messages.size());
        return messages.get(messages.size() - 1).getMessage();
    }

    public synchronized void forEach(Consumer<? super TargetedMessage> action) {
        messages.forEach(action);
    }
}
