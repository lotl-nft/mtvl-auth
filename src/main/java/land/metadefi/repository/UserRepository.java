package land.metadefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import land.metadefi.AuthConfig;
import land.metadefi.enumrable.UserStatus;
import land.metadefi.model.Auth;
import land.metadefi.model.UserEntity;
import land.metadefi.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UserEntity> {

    @Inject
    AuthConfig authConfig;

    public Auth authWithPassword(String username, String password) {
        UserEntity userEntity = find("username", username).firstResult();
        if(!validateUser(userEntity))
            return new Auth();
        if(!BCrypt.checkpw(password, userEntity.getPassword()))
            return new Auth();
        return generateAuthResource(userEntity);
    }

    public Auth authWithMetamaskToken(String token) {
        String contractAddress = AuthUtils.getContractAddressFromMetamask(token);
        UserEntity userEntity = find("contractAddress", contractAddress).firstResult();
        // TODO: if user does not have account, create for them!
        if(!validateUser(userEntity))
            return new Auth();
        return generateAuthResource(userEntity);
    }

    private boolean validateUser(UserEntity userEntity) {
        if(Objects.isNull(userEntity))
            return false;
        if(userEntity.getStatus() != UserStatus.ACTIVE.getStatus())
            return false;
        return true;
    }

    private Auth generateAuthResource(UserEntity userEntity) {
        Auth auth = new Auth();
        String token = AuthUtils.generateJwtToken(authConfig, userEntity);
        auth.setUsername(Optional.ofNullable(userEntity.getUsername()).orElse(""));
        auth.setJwtToken(token);
        return auth;
    }
}