package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "10") long size,
                                    @RequestParam(defaultValue = "0") long from,
                                    @RequestParam(defaultValue = "desc") String sort) {
        if (sort == null || (!sort.equals("asc") && !sort.equals("desc"))) {
            throw new ParameterNotValidException(sort, "Допустимые варианты сортировки - asc/desc");
        }
        if (size <= 0) {
            throw new ParameterNotValidException(size + "", "Размер выборки должен быть больше нуля");
        }
        if (from < 0) {
            throw new ParameterNotValidException(from + "", "Параметр from должен быть не меньше нуля");
        }

        return postService.findAll(size, from, sort);
    }

    @GetMapping("/{postId}")
    public Post find(@PathVariable long postId) {
        return postService.find(postId).get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}