package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import static ru.yandex.practicum.filmorate.validators.UserValidator.isUserInfoValid;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        try {
            if (isUserInfoValid(user) && !isEmailDuplicated(user.getEmail())) {
                user.setId(getNextUserId());

                if (user.getName() == null) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.info("Пользователь создан: {}", user);
            }
        } catch (ValidationException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        if (updatedUser.getId() == null) {
            log.warn("Не указан id пользователя для обновления");
            throw new ValidationException("Id должен быть указан.");
        }

        if (users.get(updatedUser.getId()) == null) {
            log.warn("Пользователь с id {} не найден", updatedUser.getId());
            throw new NotFoundException("Пользователь с указанным id = " + updatedUser.getId() + " не найден.");
        }

        try {
            isUserInfoValid(updatedUser);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
            throw exception;
        }

        if (isEmailDuplicated(updatedUser.getEmail())) {
            log.warn("Email {} уже используется", updatedUser.getEmail());
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        users.put(updatedUser.getId(), updatedUser);
        log.info("Обновленный пользователь {} сохранен", updatedUser);
        return updatedUser;
    }

    @Override
    public User findUserById(Integer id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", id)));
    }

    private boolean isEmailDuplicated(String newEmail) {
        return users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(newEmail));
    }

    private Integer getNextUserId() {
        return ++id;
    }
}
