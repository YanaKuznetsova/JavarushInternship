package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
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

    public MealServlet(){
        super();
        mealRepository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("id"));
            mealRepository.deleteMeal(id);
            forward = LIST_MEAL;
            request.setAttribute("mealsList", mealRepository.getAllMeals());
        } else if (action != null && action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal mealToEdit = mealRepository.getMealById(id);
            request.setAttribute("mealToEdit", mealToEdit);
        } else if (action != null && action.equalsIgnoreCase("listMeals")){
            forward = LIST_MEAL;
            request.setAttribute("mealsList", mealRepository.getAllMeals());
        } else {
            forward = INSERT_OR_EDIT;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("dateTime"));
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException ignored) {}
        if(id > 0) {
            mealRepository.updateMeal(dateTime, description, calories, id);
        } else {
            mealRepository.addMeal(dateTime, description, calories);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("mealsList", mealRepository.getAllMeals());
        view.forward(request, response);
    }

}