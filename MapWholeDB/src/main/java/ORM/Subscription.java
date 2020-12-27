package ORM;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Subscriptions")
public class Subscription {
    @EmbeddedId
    private SubscriptionKey subscriptionKey;

    @Column(name = "subscription_date")
    @NotNull
    private LocalDateTime subscriptionDate;

    public Subscription() {
    }

    public SubscriptionKey getSubscriptionKey() {
        return subscriptionKey;
    }

    public Subscription(Student student, Course course) {
        this.subscriptionKey = new SubscriptionKey(student, course);
    }

    public LocalDateTime getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDateTime subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
