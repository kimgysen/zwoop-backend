package be.zwoop.domain.model.user;

import be.zwoop.domain.model.tag.TagDto;
import be.zwoop.repository.user.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class UserFullDto {
    UUID userId;
    String firstName;
    String lastName;
    String nickName;
    String avatar;
    String email;
    String aboutText;
    Set<TagDto> tags;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static UserFullDto fromEntity(UserEntity userEntity) {
        return UserFullDto.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .nickName(userEntity.getNickName())
                .avatar(userEntity.getAvatar())
                .email(userEntity.getEmail())
                .aboutText(userEntity.getAboutText())
                .tags(TagDto.fromTagSet(userEntity.getTags()))
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

}
