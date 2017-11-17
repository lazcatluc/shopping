package ro.contezi.shopping.list;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    @Id
    private String id;
    @Column
    @JsonProperty("first_name")
    private String firstName;
    @Column
    @JsonProperty("last_name")
    private String lastName;
    @Column
    @JsonProperty("profile_pic")
    private String profilePic;
    @Column
    private String gender;
    @Column
    private String locale;
    @Column
    private int timezone;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @OrderBy("createdDate DESC")
    private Set<ShoppingList> myLists;
    @ManyToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @OrderBy("shareDate DESC")
    private Set<SharedList> listSharedWithMe;
    
    public Set<ShoppingList> getMyLists() {
        return Collections.unmodifiableSet(myLists);
    }

    public Set<SharedList> getListSharedWithMe() {
        return Collections.unmodifiableSet(listSharedWithMe);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
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
        Author other = (Author) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Author [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
