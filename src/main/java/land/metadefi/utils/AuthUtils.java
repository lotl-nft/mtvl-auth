package land.metadefi.utils;

import io.smallrye.jwt.build.Jwt;
import land.metadefi.AuthConfig;
import land.metadefi.model.UserEntity;
import org.eclipse.microprofile.jwt.Claims;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

public class AuthUtils {

    public static String generateJwtToken(AuthConfig config, UserEntity userEntity) {
        return Jwt.issuer(config.issuer())
                .upn(Optional.ofNullable(userEntity.getUsername()).orElse(""))
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.address.name(), Optional.ofNullable(userEntity.getContractAddress()).orElse(""))
                .sign();
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static String getContractAddressFromMetamask(String token) {
        return "";
    }

}