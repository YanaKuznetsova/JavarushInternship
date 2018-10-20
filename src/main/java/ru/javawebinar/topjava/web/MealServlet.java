package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";

    private ConfigurableApplicationContext springApplicationContext;
    private MealRestController mealController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        springApplicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = springApplicationContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        springApplicationContext.close();
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");
        action = action == null ? "listMeals" : action;

        if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            log.info("Delete {}", mealController.get(id));
            mealController.delete(id);
            forward = LIST_MEAL;
            request.setAttribute("mealsList", mealController.getAll());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal mealToEdit = mealController.get(id);
            log.info("Edit {}", mealToEdit);
            request.setAttribute("mealToEdit", mealToEdit);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = LIST_MEAL;
            request.setAttribute("mealsList", mealController.getAll());
        } else {
            forward = INSERT_OR_EDIT;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("dateTime"));
            String stringId = request.getParameter("id");
            Integer id = stringId.isEmpty() ? null : Integer.valueOf(stringId);
            Meal meal = new Meal(dateTime, description, calories, id);
            if (meal.isNew()) {
                mealController.create(meal);
                log.info("Create");
            } else {
                mealController.update(meal, id);
                log.info("Update id = {}", id);
            }
            RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
            request.setAttribute("mealsList", mealController.getAll());
            view.forward(request, response);
        } else if (action.equalsIgnoreCase("filter")) {
            LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
            LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
            LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
            LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
            request.setAttribute("mealsList", mealController.getBetween(startDate, startTime, endDate, endTime));
            request.getRequestDispatcher(LIST_MEAL).forward(request, response);
        }
    }

}