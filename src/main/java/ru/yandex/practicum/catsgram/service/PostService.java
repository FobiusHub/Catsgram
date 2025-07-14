package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
@RequiredArgsConstructor
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public Collection<Post> findAll(long size, long from, String sort) {
        if (size < 0) {
            throw new ConditionsNotMetException("Размер должен быть больше нуля");
        }
        Collection<Post> postsList;
        if (sort.equals("asc")) {
            postsList = posts.values().stream()
                    .sorted(Comparator.comparing(Post::getPostDate))
                    .skip(from)
                    .limit(size)
                    .toList();
        } else if (sort.equals("desc")) {
            postsList = posts.values().stream()
                    .sorted(Comparator.comparing(Post::getPostDate).reversed())
                    .skip(from)
                    .limit(size)
                    .toList();
        } else {
            throw new ConditionsNotMetException("Неверный параметр сортировки");
        }
        return postsList;
    }

    public Post find(long id) {
        if (posts.containsKey(id)) {
            return posts.get(id);
        } else {
            throw new NotFoundException("Пост с id " + id + " не существует");
        }
    }

    public Post create(Post post) {
        long authorId = post.getAuthorId();
        Optional<User> userOptional = userService.findUserById(authorId);
        if (userOptional.isEmpty()) {
            throw new ConditionsNotMetException("«Автор с id = " + authorId + " не найден»");
        }
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}