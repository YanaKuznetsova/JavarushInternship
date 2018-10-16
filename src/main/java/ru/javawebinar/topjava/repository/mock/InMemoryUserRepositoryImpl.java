package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private static AtomicInteger idCounter = new AtomicInteger(0);
    private static Map<Integer, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        User user = repository.remove(id);
        return user != null;
    }

    @Override
    public User save(User user) {
        User result;
        if (user.isNew()) {
            user.setId(idCounter.getAndIncrement());
            repository.put(user.getId(), user);
            result = user;
        } else {
            result = repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
        }
        log.info("Save meal with id = {}", result.getId());
        return result;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<>(repository.values())
                .stream()
                .sorted(Comparator.comparing(User::getName)
                        .thenComparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values()
                .stream()
                .filter((u) -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Couldn't find a user");
                    return null;
                });
    }
}
