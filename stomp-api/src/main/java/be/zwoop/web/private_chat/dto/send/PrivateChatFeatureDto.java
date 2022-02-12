package be.zwoop.web.private_chat.dto.send;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PrivateChatFeatureDto<T> {
    PrivateChatFeatureType featureType;
    T featureDto;
}
