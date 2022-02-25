package be.zwoop.service.bidding;


import be.zwoop.amqp.domain.model.UserDto;
import be.zwoop.amqp.domain.post.feature.bidding.BiddingAddedDto;
import be.zwoop.amqp.domain.post.feature.bidding.BiddingChangedDto;
import be.zwoop.amqp.domain.post.feature.bidding.BiddingRemovedDto;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class BiddingFactory {
    public BiddingAddedDto buildBiddingAddedDto(BiddingEntity biddingEntity) {
        UserEntity consultant = biddingEntity.getConsultant();
        return BiddingAddedDto.builder()
                .biddingId(biddingEntity.getBiddingId())
                .consultant(UserDto.builder()
                        .userId(consultant.getUserId())
                        .nickName(consultant.getNickName())
                        .avatar(consultant.getProfilePic())
                        .build())
                .askPrice(biddingEntity.getAskPrice())
                .build();

    }

    public BiddingChangedDto buildBiddingChangedDto(BiddingEntity biddingEntity) {
        UserEntity consultant = biddingEntity.getConsultant();
        return BiddingChangedDto.builder()
                .biddingId(biddingEntity.getBiddingId())
                .consultant(UserDto.builder()
                        .userId(consultant.getUserId())
                        .nickName(consultant.getNickName())
                        .avatar(consultant.getProfilePic())
                        .build())
                .askPrice(biddingEntity.getAskPrice())
                .build();
    }

    public BiddingRemovedDto buildBiddingRemovedDto(BiddingEntity biddingEntity) {
        UserEntity consultant = biddingEntity.getConsultant();
        return BiddingRemovedDto.builder()
                .biddingId(biddingEntity.getBiddingId())
                .consultant(UserDto.builder()
                        .userId(consultant.getUserId())
                        .nickName(consultant.getNickName())
                        .avatar(consultant.getProfilePic())
                        .build())
                .build();
    }

}
