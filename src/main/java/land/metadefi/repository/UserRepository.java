package land.metadefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import land.metadefi.AuthConfig;
import land.metadefi.enumrable.UserStatus;
import land.metadefi.error.CredentialsInvalidException;
import land.metadefi.error.UserInactiveException;
import land.metadefi.model.Auth;
import land.metadefi.entity.UserEntity;
import land.metadefi.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

@Slf4j
@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UserEntity> {

    @Inject
    AuthConfig authConfig;

    @ConfigProperty(name = "smallrye.jwt.token.kid")
    String kid;

    public Auth authWithPassword(String username, String password) {
        UserEntity userEntity = find("username", username).firstResult();
        if (Objects.isNull(userEntity))
            throw new CredentialsInvalidException();
        if (!BCrypt.checkpw(password, userEntity.getPassword()))
            throw new CredentialsInvalidException();
        if (!isActive(userEntity))
            throw new UserInactiveException();
        return generateAuthResource(userEntity);
    }

    public Auth authWithMetamaskSignature(String address, String message, String signature) {
        if (!AuthUtils.validateAddressFromMetamask(address, message, signature))
            throw new CredentialsInvalidException();

        UserEntity userEntity = find("contractAddress", address).firstResult();
        if (Objects.isNull(userEntity)) {
            userEntity = new UserEntity();
            userEntity.setAddress(address);
            userEntity.setStatus(UserStatus.ACTIVE.getStatus());
            persist(userEntity);
        }
        if (!isActive(userEntity))
            throw new UserInactiveException();
        return generateAuthResource(userEntity);
    }

    public Auth renewToken(String address) {
        if (StringUtils.isEmpty(address))
            throw new CredentialsInvalidException();

        UserEntity userEntity = find("contractAddress", address).firstResult();
        if (!isActive(userEntity))
            throw new UserInactiveException();

        return generateAuthResource(userEntity);
    }

    private boolean isActive(UserEntity userEntity) {
        return Objects.equals(userEntity.getStatus(), UserStatus.ACTIVE.getStatus());
    }

    private Auth generateAuthResource(UserEntity userEntity) {
        Auth auth = new Auth();
        auth.setJwtToken(AuthUtils.generateJwtToken(authConfig, kid, userEntity));
        auth.setJwtRefreshToken(AuthUtils.generateJwtRefreshToken(authConfig, kid, userEntity));
        return auth;
    }
}