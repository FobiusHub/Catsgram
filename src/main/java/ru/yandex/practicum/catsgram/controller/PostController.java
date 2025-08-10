package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody NewPostRequest request) {
        return postService.createPost(request);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable("postId") long postId, @RequestBody UpdatePostRequest request) {
        return postService.updatePost(postId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Post> getPosts(@RequestParam(defaultValue = "10") long size,
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

        return postService.getPosts(size, from, sort);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Post find(@PathVariable("postId") long postId) {
        return postService.getPostById(postId);
    }




}