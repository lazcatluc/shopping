package ro.contezi.shopping.list.action.item;

import org.springframework.beans.factory.annotation.Value;
import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.list.ShoppingListItem;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ItemSuggestions {
    private final ShoppingListItemJpaRepository shoppingListItemJpaRepository;
    private final int suggestionsSize;

    public ItemSuggestions(ShoppingListItemJpaRepository shoppingListItemJpaRepository,
                           @Value("${suggestionsSize:5}") int suggestionsSize) {
        this.shoppingListItemJpaRepository = shoppingListItemJpaRepository;
        this.suggestionsSize = suggestionsSize;
    }

    List<String> getSuggestions(Author author) {
        return getSuggestions(author, suggestionsSize);
    }

    List<String> getSuggestions(Author author, int howMany) {
        Set<ShoppingListItem> previouslyAdded = shoppingListItemJpaRepository
                .findByShoppingList_AuthorOrShoppingList_Shares_Author(author, author);
        Map<String, List<ZonedDateTime>> itemsWithAddDates = new HashMap<>();
        previouslyAdded.forEach(item -> itemsWithAddDates.computeIfAbsent(item.getItemName(), s -> new ArrayList<>())
                    .add(item.getAddedDate()));
        List<ItemWithAddDates> itemWithAddDates = itemsWithAddDates.entrySet().stream().map(entry ->
                new ItemWithAddDates(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        Collections.sort(itemWithAddDates, ItemWithAddDates.comparator());
        return itemWithAddDates.stream().limit(howMany).map(ItemWithAddDates::getName).collect(Collectors.toList());
    }
}
