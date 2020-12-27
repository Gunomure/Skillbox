package ORM;

import com.sun.istack.NotNull;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscriptionKey implements Serializable {
    @ManyToOne
    @JoinColumn(name = "student_id")
    @NotNull
    private Student student;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    public SubscriptionKey() {
    }

    public SubscriptionKey(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourse(), getStudent());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionKey)) return false;
        SubscriptionKey that = (SubscriptionKey) o;
        return Objects.equals(getStudent(), that.getStudent()) &&
                Objects.equals(getCourse(), that.getCourse());
    }
}