package ORM.notifications;

import ORM.Student;
import ORM.Teacher;

import javax.persistence.*;

@Entity
@DiscriminatorValue("CommentAlert")
public class CommentAlert extends Notification {
    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private Student sender;

    public Student getSender() {
        return sender;
    }

    public CommentAlert() {
    }

    public CommentAlert(Teacher receiver, String text, String title, Student sender) {
        super(receiver, text, title);
        this.sender = sender;
    }

    public void setSender(Student sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "CommentAlert{" +
                "sender=" + sender +
                '}';
    }
}
