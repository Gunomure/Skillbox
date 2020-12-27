package main.controllers;

import lombok.extern.slf4j.Slf4j;
import main.dto.ActionModel;
import main.orm.Action;
import main.services.ActionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ActionControllerRest {

    private final ActionService actionService;

    ActionControllerRest(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping("/api/todo")
    public List<ActionModel> getAllActions() throws Exception {
        log.debug("Get all actions");
        return actionService.getAll()
                .stream()
                .map(ActionModel::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/todo/{id}")
    public ActionModel getById(@PathVariable int id) {
        log.debug("Get action number {}", id);
        return ActionModel.map(actionService.getById(id));
    }

    @PostMapping("/api/todo")
    public ActionModel addAction(@Validated(ActionModel.Create.class) @RequestBody ActionModel action) {
        log.debug("Add action");
        Action addedAction = actionService.addAction(modelToAction(action));
        return ActionModel.map(addedAction);
    }

    @PutMapping("/api/todo/{id}")
    public ActionModel changeAction(@Validated(ActionModel.Update.class) @RequestBody ActionModel action,
                                    @PathVariable int id) {
        log.debug("Change action number {}", id);

        Action changedAction = actionService.changeAction(modelToAction(action), id);
        return ActionModel.map(changedAction);
    }

    @PutMapping(value = "/api/todo")
    public List<ActionModel> changeActions(@Validated(ActionModel.Update.class) @RequestBody List<ActionModel> actions) {
        log.debug("Change {} actions", actions.size());
        return actionService.changeActions(modelToAction(actions))
                .stream()
                .map(ActionModel::map)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/api/todo")
    public void deleteAllActions() {
        log.debug("Delete all actions");
        actionService.deleteAllActions();
    }

    @DeleteMapping("/api/todo/{id}")
    public void deleteAction(@PathVariable int id) {
        log.debug("Delete action number {}", id);
        actionService.deleteAction(id);
    }

    private static Action modelToAction(ActionModel actiontoConvert) {
        return new Action(actiontoConvert.getId(),
                actiontoConvert.getToDo(),
                actiontoConvert.getCreated(),
                actiontoConvert.getDeadline());
    }

    private static List<Action> modelToAction(List<ActionModel> actions) {
        List<Action> result = new ArrayList<>();
        for (ActionModel actionModel : actions) {
            result.add(modelToAction(actionModel));
        }
        return result;
    }
}
