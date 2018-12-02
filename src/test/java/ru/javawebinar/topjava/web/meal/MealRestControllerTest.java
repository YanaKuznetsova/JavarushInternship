package ru.javawebinar.topjava.web.meal;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.readFromJsonResultActions;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.convertToExcess;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExcess;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + USER_MEAL_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, Meal.class), USER_MEAL_0));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(getToMatcher(getWithExcess(USER_MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void testCreateWithLocation() throws Exception {
        Meal expected = new Meal(LocalDateTime.now(), "One more meal", 1000, null);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJsonResultActions(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(USER_ID), expected, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3,
                USER_MEAL_2, USER_MEAL_1, USER_MEAL_0);

    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = new Meal(USER_MEAL_0);
        updated.setDescription("Updated description");
        mockMvc.perform(put(REST_URL + USER_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(USER_MEAL_ID, USER_ID), updated);
    }


    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_MEAL_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mealService.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3,
                USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    void testGetBetween() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2015, Month.MAY, 30, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, Month.MAY, 31, 14, 0);
        mockMvc.perform(post(REST_URL + "/filter")
                .param("startDate", startDateTime.toLocalDate().toString())
                .param("startTime", startDateTime.toLocalTime().toString())
                .param("endDate", endDateTime.toLocalDate().toString())
                .param("endTime", endDateTime.toLocalTime().toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(getToMatcher(convertToExcess(USER_MEAL_4, false),
                        convertToExcess(USER_MEAL_3, false),
                        convertToExcess(USER_MEAL_2, false),
                        convertToExcess(USER_MEAL_1, false),
                        convertToExcess(USER_MEAL_0, false)));
    }

}
