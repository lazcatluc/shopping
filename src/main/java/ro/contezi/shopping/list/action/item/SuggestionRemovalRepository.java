package ro.contezi.shopping.list.action.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.contezi.shopping.author.Author;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface SuggestionRemovalRepository extends JpaRepository<SuggestionRemoval, String> {
    Optional<SuggestionRemoval> findByAuthorAndItemName(Author author, String item);

    default SuggestionRemoval removeItem(Author author, String item) {
        SuggestionRemoval suggestionRemoval = intializedSuggestionRemoval(author, item);
        suggestionRemoval.setLastRemoval(ZonedDateTime.now());
        return save(suggestionRemoval);
    }

    default SuggestionRemoval intializedSuggestionRemoval(Author author, String item) {
        return findByAuthorAndItemName(author, item).orElseGet(() -> {
                SuggestionRemoval newSuggestionRemoval = new SuggestionRemoval();
                newSuggestionRemoval.setAuthor(author);
                newSuggestionRemoval.setItemName(item);
                newSuggestionRemoval.setLastRemoval(ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault()));
                return newSuggestionRemoval;
            });
    }
}
