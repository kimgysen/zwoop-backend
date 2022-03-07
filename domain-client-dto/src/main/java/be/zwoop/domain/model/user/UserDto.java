package be.zwoop.domain.model.user;


import be.zwoop.repository.user.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class UserDto implements Serializable {
    UUID userId;
    String nickName;
    String avatar;

    public static UserDto fromUserEntity(UserEntity userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .nickName(userEntity.getNickName())
                .avatar(userEntity.getProfilePic())
                .build();
    }
}
