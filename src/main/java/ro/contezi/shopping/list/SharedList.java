package ro.contezi.shopping.list;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SharedList {
    @Id
    private String id = UUID.randomUUID().toString();
    @ManyToOne
    private Author author;
    @ManyToOne
    private ShoppingList shoppingList;
    @Column
    private ZonedDateTime shareDate = ZonedDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ZonedDateTime getShareDate() {
        return shareDate;
    }

    public void setShareDate(ZonedDateTime shareDate) {
        this.shareDate = shareDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, shoppingList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        SharedList other = (SharedList) obj;
        return Objects.equals(author, other.author) &&
                Objects.equals(shoppingList, other.shoppingList);
    }


}
