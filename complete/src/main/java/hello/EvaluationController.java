package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller
public class EvaluationController {

    @GetMapping("/eval")
    public String evaluate(Model model) {
        model.addAttribute("evaluator", new Evaluator());
        return "evaluation";
    }

    @PostMapping("/eval")
    public String showResult(@ModelAttribute Evaluator evaluator) {
        try {
            evaluator.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "evaluationResult";
    }
}
