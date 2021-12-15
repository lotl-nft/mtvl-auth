package land.metadefi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {
    String jwtToken;
    String jwtRefreshToken;
}
