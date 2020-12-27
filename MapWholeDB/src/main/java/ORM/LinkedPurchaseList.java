package ORM;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "LinkedPurchaseList")
public class LinkedPurchaseList {

    @EmbeddedId
    private LinkedPurchaseListKey linkedPurchaseListKey;

    private int price;
    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    public LinkedPurchaseList(Student student, Course course) {
        this.linkedPurchaseListKey = new LinkedPurchaseListKey(student, course);
    }

    public LinkedPurchaseList() {
    }

    public LinkedPurchaseListKey getLinkedPurchaseListKey() {
        return linkedPurchaseListKey;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDateTime subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
