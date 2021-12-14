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

    private AuthUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateJwtToken(AuthConfig config, UserEntity userEntity) {
        return Jwt.issuer(config.issuer())
                .audience(Optional.of(userEntity.getId().toString()).orElse(""))
                .upn(Optional.ofNullable(userEntity.getEmail()).orElse(""))
                .preferredUserName(Optional.ofNullable(userEntity.getUsername()).orElse(""))
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.address.name(), Optional.ofNullable(userEntity.getContractAddress()).orElse(""))
                .sign();
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean validateAddressFromMetamask(String address, String message, String signature) {
        return address.equals(Web3Utils.recoverAddressFromSignature(address, message, signature));
    }

}