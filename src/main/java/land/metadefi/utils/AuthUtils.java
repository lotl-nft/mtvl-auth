package land.metadefi.utils;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import land.metadefi.AuthConfig;
import land.metadefi.enumrable.UserRole;
import land.metadefi.entity.UserEntity;
import lombok.experimental.UtilityClass;
import org.eclipse.microprofile.jwt.Claims;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class AuthUtils {

    public static String generateJwtToken(AuthConfig config, String kid, UserEntity userEntity) {
        return jwtBuilder(kid, userEntity)
            .issuer(config.issuerToken())
            .sign();
    }

    public static String generateJwtRefreshToken(AuthConfig config, String kid, UserEntity userEntity) {
        return jwtBuilder(kid, userEntity)
            .issuer(config.issuerRefreshToken())
            .expiresIn(config.jwtRefreshToken())
            .innerSign()
            .encrypt();
    }

    static JwtClaimsBuilder jwtBuilder(String kid, UserEntity userEntity) {
        return Jwt.audience(Optional.of(userEntity.getId().toString()).orElse(""))
            .upn(Optional.ofNullable(userEntity.getEmail()).orElse(""))
            .preferredUserName(Optional.ofNullable(userEntity.getUsername()).orElse(""))
            .groups(new HashSet<>(List.of(UserRole.USER.getValue())))
            .claim(Claims.address.name(), Optional.ofNullable(userEntity.getAddress()).orElse(""))
            .claim(Claims.kid.name(), kid);
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean validateAddressFromMetamask(String address, String message, String signature) {
        return address.equals(Web3Utils.recoverAddressFromSignature(address, message, signature));
    }

}