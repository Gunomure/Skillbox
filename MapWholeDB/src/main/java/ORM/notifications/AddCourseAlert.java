package ORM.notifications;

import ORM.Course;
import ORM.Teacher;

import javax.persistence.*;

@Entity
@DiscriminatorValue("AddCourseAlert")
public class AddCourseAlert extends Notification {
    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private Course sender;

    public AddCourseAlert() {
    }

    public AddCourseAlert(Teacher receiver, String text, String title, Course sender) {
        super(receiver, text, title);
        this.sender = sender;
    }

    public Course getSender() {
        return sender;
    }

    public void setSender(Course sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "AddCourseAlert{" +
                "sender=" + sender +
                '}';
    }
}
