package land.metadefi;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "auth")
public interface AuthConfig {

    @WithName("issuer")
    String issuer();

}