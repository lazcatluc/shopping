package ro.contezi.shopping.list;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.contezi.shopping.author.Author;

@Entity
public class ShoppingList {
    @Id
    private String id = UUID.randomUUID().toString();
    @ManyToOne
    private Author author;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("shareDate DESC")
    private Set<SharedList> shares;
    @Column
    private ZonedDateTime createdDate = ZonedDateTime.now();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("addedDate")
    private Set<ShoppingListItem> items = new LinkedHashSet<>();
    
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

    public ShoppingListItem removeItem(String item) {
        ShoppingListItem myItem = items.stream().filter(contains(item))
                .findFirst().orElseGet(() -> addItem(item));
        items.remove(myItem);
        return myItem;
    }
    
    public boolean shareWith(Author author) {
        SharedList share = new SharedList();
        share.setAuthor(author);
        share.setShoppingList(this);
        return shares.add(share);
    }

    public boolean isSharedWith(Author anotherAuthor) {
        return shares.stream().map(SharedList::getAuthor).anyMatch(anotherAuthor::equals);
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

    @JsonIgnore
    public Set<SharedList> getShares() {
        return shares;
    }

    public void setShares(Set<SharedList> shares) {
        this.shares = shares;
    }

    public Set<Author> getAllInterestedParties() {
        Set<Author> authors = new HashSet<>();
        if (author != null) {
            authors.add(author);
        }
        if (shares != null) {
            shares.stream().map(SharedList::getAuthor).forEach(authors::add);
        }
        return authors;
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
