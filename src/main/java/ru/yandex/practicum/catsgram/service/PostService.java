package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.dto.UserDto;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public Post createPost(NewPostRequest request) {
        long authorId = request.getAuthorId();
        //Запрашиваем пользователя по-указанному id. Если пользователь не существует userService выбросит исключение.
        UserDto userOptional = userService.getUserById(authorId);

        Post post = PostMapper.mapToPost(request);

        post = postRepository.save(post);

        return post;
    }

    public Post getPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id " + postId + " не существует"));
    }

    public Collection<Post> getPosts(long size, long from, String sort) {
        Collection<Post> postsList;
        if (sort.equals("asc")) {
            postsList = postRepository.findAll().stream()
                    .sorted(Comparator.comparing(Post::getPostDate))
                    .skip(from)
                    .limit(size)
                    .toList();
        } else {
            postsList = postRepository.findAll().stream()
                    .sorted(Comparator.comparing(Post::getPostDate).reversed())
                    .skip(from)
                    .limit(size)
                    .toList();
        }
        return postsList;
    }

    public Post updatePost(long postId, UpdatePostRequest request) {
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(post, request))
                .orElseThrow(() -> new NotFoundException("Пост не найден"));
        updatedPost = postRepository.update(updatedPost);
        return updatedPost;
    }
}