package ro.contezi.shopping.list.action.item;

import ro.contezi.shopping.author.Author;
import ro.contezi.shopping.list.ShoppingListItem;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ItemSuggestions {
    private final ShoppingListItemJpaRepository shoppingListItemJpaRepository;
    private final int suggestionsSize;
    private final int minimumBuyThreshold;
    private final SuggestionRemovalRepository suggestionRemovalRepository;

    public ItemSuggestions(ShoppingListItemJpaRepository shoppingListItemJpaRepository,
                           int suggestionsSize, int minimumBuyThreshold,
                           SuggestionRemovalRepository suggestionRemovalRepository) {
        this.shoppingListItemJpaRepository = shoppingListItemJpaRepository;
        this.suggestionsSize = suggestionsSize;
        this.minimumBuyThreshold = minimumBuyThreshold;
        this.suggestionRemovalRepository = suggestionRemovalRepository;
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
        List<ItemWithAddDates> itemWithAddDates = itemsWithAddDates.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= minimumBuyThreshold)
                .map(entry -> new ItemWithAddDates(entry.getKey(), entry.getValue()))
                .filter(item -> item.getLastAdded().isAfter(suggestionRemovalRepository
                        .intializedSuggestionRemoval(author, item.getName())
                        .getLastRemoval()))
                .collect(Collectors.toList());
        Collections.sort(itemWithAddDates, ItemWithAddDates.comparator());
        return itemWithAddDates.stream().limit(howMany).map(ItemWithAddDates::getName).collect(Collectors.toList());
    }
}
