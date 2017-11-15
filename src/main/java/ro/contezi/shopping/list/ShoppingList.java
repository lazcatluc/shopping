package ro.contezi.shopping.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

@Entity
public class ShoppingList {
    @Id
    private String id = UUID.randomUUID().toString();
    @Column
    private String author;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Boolean> items = new LinkedHashMap<>();
    
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
    
    public Map<String, Boolean> getItems() {
        return Collections.unmodifiableMap(items);
    }
    
    public void setItems(Map<String, Boolean> items) {
        this.items = new HashMap<>(items);
    }

    public void addItem(String item) {
        items.put(item, false);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public void buyItem(String item) {
        items.put(item, true);
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
    
}
