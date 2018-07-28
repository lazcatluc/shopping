package ro.contezi.shopping.list.action.item;

import ro.contezi.shopping.author.Author;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class SuggestionRemoval {
    @Id
    private String id = UUID.randomUUID().toString();
    @ManyToOne
    private Author author;
    @Column
    private String itemName;
    @Column
    private ZonedDateTime lastRemoval;

    public ZonedDateTime getLastRemoval() {
        return lastRemoval;
    }

    public void setLastRemoval(ZonedDateTime lastRemoval) {
        this.lastRemoval = lastRemoval;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
