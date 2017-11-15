package ro.contezi.shopping.list;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ShoppingListItem {
    @Id
    private String id = UUID.randomUUID().toString();
    @Column
    private String itemName;
    @ManyToOne
    private ShoppingList shoppingList;
    @Column
    private ZonedDateTime addedDate = ZonedDateTime.now();
    @Column
    private ZonedDateTime boughtDate;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ZonedDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(ZonedDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public ZonedDateTime getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(ZonedDateTime boughtDate) {
        this.boughtDate = boughtDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, shoppingList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return Objects.equals(this.itemName, ((ShoppingListItem)obj).itemName)&&
               Objects.equals(this.shoppingList, ((ShoppingListItem)obj).shoppingList);
    }

    @Override
    public String toString() {
        return itemName + "=" + (boughtDate!=null);
    }
    
}
