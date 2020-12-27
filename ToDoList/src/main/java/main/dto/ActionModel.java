package main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.orm.Action;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ActionModel {
    @Null(groups = {Create.class})
    @NotNull(groups = {Update.class})
    @Min(0)
    private Integer id;

    @NotNull(groups = {Create.class, Update.class})
    @Size(min = 1, max = 200,
            message = "Action describe must be between 1 and 200 characters",
            groups = {Create.class, Update.class})
    private String toDo;

    @NotNull(groups = {Create.class, Update.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent(message = "Action must be created in the past",
            groups = {Create.class, Update.class})
    private LocalDateTime created;

    @NotNull(groups = {Create.class, Update.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "Deadline must be in the future",
            groups = {Create.class, Update.class})
    private LocalDateTime deadline;

    public static ActionModel map(Action action) {
        ActionModel actionModel = new ActionModel();
        actionModel.setId(action.getId());
        actionModel.setToDo(action.getToDo());
        actionModel.setCreated(action.getCreated());
        actionModel.setDeadline(action.getDeadline());
        return actionModel;
    }

    public interface Create {
    }

    public interface Update {
    }

}
