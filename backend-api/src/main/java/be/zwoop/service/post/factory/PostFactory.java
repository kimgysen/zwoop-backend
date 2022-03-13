package be.zwoop.service.post.factory;

import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.SavePostDto;

import java.util.List;

public interface PostFactory {
    PostEntity buildPostFromDto(SavePostDto savePostDto, UserEntity opEntity);
    List<Long> collectTagIdsByPostDto(SavePostDto savePostDto);
}
