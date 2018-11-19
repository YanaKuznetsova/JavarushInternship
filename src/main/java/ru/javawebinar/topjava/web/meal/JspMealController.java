package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    private static final String INSERT_OR_EDIT = "mealForm";
    private static final String LIST_MEAL = "meals";

    public JspMealController(MealService service) {
        super(service);
    }

    //    @GetMapping("/meals")
    @RequestMapping(method = RequestMethod.GET)
    public String mealsList(Model model) {
        model.addAttribute("mealsList", getAll());
        return LIST_MEAL;
    }

    //    @GetMapping("/meals/delete/{id}")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteMeal(Model model, @PathVariable int id) {
        delete(id);
        model.addAttribute("mealsList", getAll());
        return LIST_MEAL;
    }

    //    @GetMapping("/meals/edit/{id}")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String updateMeal(Model model, @PathVariable int id) {
        Meal meal = get(id);
        model.addAttribute("mealToEdit", meal);
        return INSERT_OR_EDIT;
    }

    //    @GetMapping("/meals/add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String insertNewMeal() {
        return INSERT_OR_EDIT;
    }

    //    @PostMapping("/meals")
    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String filter(Model model, @ModelAttribute Meal meal, HttpServletRequest request) throws UnsupportedEncodingException {
        LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
        LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
        LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
        LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
        request.setAttribute("mealsList", getBetween(startDate, startTime, endDate, endTime));
        return LIST_MEAL;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("dateTime"));
        String stringId = request.getParameter("id");
        Integer id = stringId.isEmpty() ? null : Integer.valueOf(stringId);
        Meal meal = new Meal(dateTime, description, calories, id);
        if (meal.isNew()) {
            create(meal);
        } else {
            update(meal, id);
        }
        model.addAttribute("mealsList", getAll());
        return "redirect:meals";
    }

}
