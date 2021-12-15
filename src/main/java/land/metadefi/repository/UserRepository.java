package land.metadefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import land.metadefi.AuthConfig;
import land.metadefi.enumrable.UserStatus;
import land.metadefi.model.Auth;
import land.metadefi.model.UserEntity;
import land.metadefi.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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

    @ConfigProperty(name = "smallrye.jwt.token.kid")
    String kid;

    public Auth authWithPassword(String username, String password) {
        UserEntity userEntity = find("username", username).firstResult();
        if(Objects.isNull(userEntity))
            return new Auth();
        if(!isActive(userEntity))
            return new Auth();
        if(!BCrypt.checkpw(password, userEntity.getPassword()))
            return new Auth();
        return generateAuthResource(userEntity);
    }

    public Auth authWithMetamaskSignature(String address, String message, String signature) {
        if(!AuthUtils.validateAddressFromMetamask(address, message, signature))
            return new Auth();

        UserEntity userEntity = find("contractAddress", address).firstResult();
        if(Objects.isNull(userEntity)) {
            userEntity = new UserEntity();
            userEntity.setContractAddress(address);
            userEntity.setStatus(UserStatus.ACTIVE.getStatus());
            persist(userEntity);
        }
        if(!isActive(userEntity))
            return new Auth();
        return generateAuthResource(userEntity);
    }

    private boolean isActive(UserEntity userEntity) {
        return Objects.equals(userEntity.getStatus(), UserStatus.ACTIVE.getStatus());
    }

    private Auth generateAuthResource(UserEntity userEntity) {
        Auth auth = new Auth();
        String token = AuthUtils.generateJwtToken(authConfig, kid, userEntity);
        auth.setJwtToken(token);
        return auth;
    }
}