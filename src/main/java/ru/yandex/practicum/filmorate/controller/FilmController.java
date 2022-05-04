package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getName())) {
            throw new ValidationException("Фильм с таким названием уже находится в базе.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        log.debug("Добавлен новый фильм: {}", film);
        films.put(film.getName(), film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if (films.containsKey(film.getName())) {
            log.debug("Обновлена информация о фильме: {}", film);
            films.replace(film.getName(), film);
        } else {
            log.debug("Добавлен новый фильм: {}", film);
            films.put(film.getName(), film);
        }
        return film;
    }
}