package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    private static final String INSERT_OR_EDIT = "mealForm";
    private static final String LIST_MEAL = "redirect:/meals";

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteMeal(Model model, @PathVariable int id) {
        super.delete(id);
        return LIST_MEAL;
    }

    @GetMapping(value = "/edit/{id}")
    public String updateMeal(Model model, @PathVariable int id) {
        Meal meal = get(id);
        model.addAttribute("mealToEdit", meal);
        return INSERT_OR_EDIT;
    }

    @GetMapping(value = "/add")
    public String insertNewMeal(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "", 1000));
        return INSERT_OR_EDIT;
    }

    @PostMapping(value = "/filter")
    public String filter(Model model, @ModelAttribute Meal meal, HttpServletRequest request) throws UnsupportedEncodingException {
        LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
        LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
        LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
        LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
        request.setAttribute("mealsList", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @PostMapping
    public String updateOrCreate(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("dateTime"));
        String stringId = request.getParameter("id");
        Integer id = stringId.isEmpty() ? null : Integer.valueOf(stringId);
        Meal meal = new Meal(dateTime, description, calories, id);
        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, id);
        }
        model.addAttribute("mealsList", getAll());
        return LIST_MEAL;
    }

}
