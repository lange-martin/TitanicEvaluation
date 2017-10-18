package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller
public class EvaluationController {

    @GetMapping("/files/{filename:.+}/eval")
    public String testFile(@ModelAttribute Evaluator evaluator, @PathVariable String filename) {
        evaluator.setPath(filename);
        try {
            evaluator.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "evaluationResult";
    }
}
