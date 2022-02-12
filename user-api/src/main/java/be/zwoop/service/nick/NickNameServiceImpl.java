package be.zwoop.service.nick;


import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;

@AllArgsConstructor
@Service
public class NickNameServiceImpl implements NickNameService {

    private final Faker faker;
    private final UserRepository userRepository;


    @Override
    public String generateFakeNickName() {
        return generateFakeNickName(0);
    }

    private String generateFakeNickName(int attempt) {
        final int MAX_RETRIES = 20;
        if (attempt <= MAX_RETRIES) {
            String nickName = generateFakeName();
            Optional<UserEntity> userOpt = userRepository.findByNickName(nickName);

            if (userOpt.isEmpty()) {
                return nickName;
            } else {
                return generateFakeNickName(++attempt);
            }
        } else {
            throw new ResponseStatusException(CONFLICT);
        }
    }

    private String generateFakeName() {
        return faker.superhero().prefix()
                + faker.name().firstName()
                + faker.address().buildingNumber();
    }

}
