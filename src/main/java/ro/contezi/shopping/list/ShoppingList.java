package ro.contezi.shopping.list;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class ShoppingList {
    @Id
    private String id = UUID.randomUUID().toString();
    @Column
    private String author;
    @Column
    private ZonedDateTime createdDate = ZonedDateTime.now();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("addedDate")
    private Set<ShoppingListItem> items = new LinkedHashSet<>();
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Set<ShoppingListItem> getItems() {
        return Collections.unmodifiableSet(items);
    }
    
    public void setItems(Set<ShoppingListItem> items) {
        this.items = items;
    }

    public ShoppingListItem addItem(String item) {
        ShoppingListItem myItem = new ShoppingListItem();
        myItem.setShoppingList(this);
        myItem.setItemName(item);
        items.add(myItem);
        return myItem;
    }

    public void removeItem(String item) {
        items.removeIf(contains(item));
    }

    private Predicate<? super ShoppingListItem> contains(String item) {
        return myItem -> myItem.getItemName().equals(item);
    }

    public ShoppingListItem buyItem(String item) {
        ShoppingListItem myItem = items.stream().filter(contains(item))
            .findFirst().orElseGet(() -> addItem(item));

        myItem.setBoughtDate(ZonedDateTime.now());
        return myItem;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return Objects.equals(this.id, ((ShoppingList)obj).id);
    }

    @Override
    public String toString() {
        return "ShoppingList [id=" + id + ", author=" + author + ", createdDate=" + createdDate + ", items=" + items
                + "]";
    }
    
}
