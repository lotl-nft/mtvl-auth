package land.metadefi;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "auth")
public interface AuthConfig {

    @WithName("issuer.token")
    String issuerToken();

    @WithName("issuer.refresh-token")
    String issuerRefreshToken();

    @WithName("jwt.refresh-token")
    Long jwtRefreshToken();

}