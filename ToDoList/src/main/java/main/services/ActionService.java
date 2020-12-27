package main.services;

import lombok.extern.log4j.Log4j;
import main.controllers.exception.BadRequestException;
import main.controllers.exception.EntityNotFoundException;
import main.dto.ActionModel;
import main.orm.Action;
import main.orm.ActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j
public class ActionService {

    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> getAll() {
        List<Action> actions = new ArrayList<>();
        try {
            actionRepository.findAll().forEach(actions::add);
            return actions;
        } catch (Exception e) {
            log.error("Can't get actions", e);
            throw new EntityNotFoundException("Can't get actions\n" + e.getMessage());
        }
    }

    public Action getById(int id) {
        return actionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Wrong index: %d", id)));
    }

    public Action addAction(Action action) {
        try {
            return actionRepository.save(action);
        } catch (Exception e) {
            log.error("Can't add action in the store", e);
            throw new BadRequestException("Can't add action in the store");
        }
    }

    public Action changeAction(Action action, int id) {
        Action actionToChange = actionRepository.findById(id).orElseThrow(() -> new BadRequestException("Can't change action"));
        actionToChange.setToDo(action.getToDo());
        actionToChange.setCreated(action.getCreated());
        actionToChange.setDeadline(action.getDeadline());
        return actionRepository.save(actionToChange);
    }

    public List<Action> changeActions(List<Action> actions) {
        List<Action> changedActions = new ArrayList<>();
        try {
            Iterable<Action> currentActions = actionRepository.findAll();
            currentActions.forEach(changedActions::add);
            if (changedActions.size() == actions.size()) {
                int i = 0;
                for (Action actionToChange : changedActions) {
                    actionToChange.setToDo(actions.get(i).getToDo());
                    actionToChange.setCreated(actions.get(i).getCreated());
                    actionToChange.setDeadline(actions.get(i).getDeadline());
                    i++;
                }
                actionRepository.saveAll(changedActions);
                return changedActions;//.stream().map(ActionModel::map).collect(Collectors.toList());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Can't change actions", e);
            throw new BadRequestException("Can't change actions");
        }
    }

    public void deleteAllActions() {
        try {
            actionRepository.deleteAll();
        } catch (Exception e) {
            log.error("Can't delete actions", e);
            throw new BadRequestException("Can't delete actions");
        }
    }

    public void deleteAction(int id) {
        try {
            actionRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Can't delete action", e);
            throw new BadRequestException("Can't delete action");
        }
    }
}
