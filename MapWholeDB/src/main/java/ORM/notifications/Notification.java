package ORM.notifications;

import ORM.Teacher;

import javax.persistence.*;

@Entity
@Table(name = "Notifications")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "SENDER")
public abstract class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
//    @MapsId
    private Teacher receiver;

    private String text;
    private String title;

    public Notification() {
    }

    public Notification(Teacher receiver, String text, String title) {
        this.receiver = receiver;
        this.text = text;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Teacher getReceiver() {
        return receiver;
    }

    public void setReceiver(Teacher receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", receiver=" + receiver +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
