package main.controllers;

import lombok.extern.log4j.Log4j;
import main.dto.ActionModel;
import main.orm.Action;
import main.services.ActionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j
public class ActionController {

    private final ActionService actionService;

    ActionController(ActionService actionService) {
        this.actionService = actionService;
    }


    @GetMapping("/")
    public String index(Model model) {
        List<ActionModel> actions = actionService.getAll()
                .stream()
                .map(ActionModel::map)
                .collect(Collectors.toList());
        model.addAttribute("actionsCount", actions.size());
        model.addAttribute("actions", actions);
        return "index";
    }

    @GetMapping("/todo")
    public String addAction(Model model) {
        model.addAttribute("action", new ActionModel());
        return "addAction";
    }

    @PostMapping("/todo")
    public String result(@ModelAttribute ActionModel action, Model model) {
        log.debug("Add action");
        actionService.addAction(modelToAction(action));
        List<ActionModel> actions = actionService.getAll()
                .stream()
                .map(ActionModel::map)
                .collect(Collectors.toList());
        model.addAttribute("actionsCount", actions.size());
        model.addAttribute("actions", actions);
        return "index";
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
