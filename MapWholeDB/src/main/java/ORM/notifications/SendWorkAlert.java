package ORM.notifications;

import ORM.Student;
import ORM.Teacher;

import javax.persistence.*;

@Entity
@DiscriminatorValue("SendWorkAlert")
public class SendWorkAlert extends Notification {
    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private Student sender;

    public SendWorkAlert(Teacher receiver, String text, String title, Student sender) {
        super(receiver, text, title);
        this.sender = sender;
    }

    public SendWorkAlert() {
    }

    public SendWorkAlert(Student sender) {
        this.sender = sender;
    }

    public Student getSender() {
        return sender;
    }

    public void setSender(Student sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "SendWorkAlert{" +
                "sender=" + sender +
                '}';
    }
}
