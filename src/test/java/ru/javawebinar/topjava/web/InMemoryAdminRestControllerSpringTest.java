package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.util.exception.ModificationRestrictionException;
import ru.javawebinar.topjava.web.user.AdminRestController;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-app-inmemory.xml"})
class InMemoryAdminRestControllerSpringTest {

    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepositoryImpl repository;

    @BeforeEach
    void setUp() throws Exception {
        repository.init();
    }

//    @Test
//    void testDelete() throws Exception {
//        controller.delete(UserTestData.USER_ID);
//        Collection<User> users = controller.getAll();
//        assertEquals(users.size(), 1);
//        assertEquals(users.iterator().next(), ADMIN);
//    }

    @Test
    void testDeleteNotFound() throws Exception {
        assertThrows(ModificationRestrictionException.class, () ->
                controller.delete(10));
    }

}
