package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";

    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        mealRepository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");
        Integer userId = SecurityUtil.authUserId();

        if (action != null && action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("id"));
            log.info("Delete {}", mealRepository.get(id, userId));
            mealRepository.delete(id, SecurityUtil.authUserId());
            forward = LIST_MEAL;
            request.setAttribute("mealsList", MealsUtil.getWithExcess(mealRepository.getAll(userId),
                    SecurityUtil.authUserCaloriesPerDay()));
        } else if (action != null && action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal mealToEdit = mealRepository.get(id, userId);
            log.info("Edit {}", mealRepository.get(id, userId));
            request.setAttribute("mealToEdit", mealToEdit);
        } else if (action != null && action.equalsIgnoreCase("listMeals")){
            forward = LIST_MEAL;
            request.setAttribute("mealsList", MealsUtil.getWithExcess(mealRepository.getAll(userId),
                    SecurityUtil.authUserCaloriesPerDay()));
        } else {
            forward = INSERT_OR_EDIT;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Integer userId = SecurityUtil.authUserId();

        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("dateTime"));
        String stringId = request.getParameter("id");
        Integer id = stringId.isEmpty() ? null : Integer.valueOf(stringId);
        Meal meal = new Meal(dateTime, description, calories, id);

        mealRepository.save(meal, userId);
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("mealsList", MealsUtil.getWithExcess(mealRepository.getAll(userId),
                SecurityUtil.authUserCaloriesPerDay()));
        view.forward(request, response);
    }

}