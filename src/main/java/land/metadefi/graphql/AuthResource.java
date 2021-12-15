package land.metadefi.graphql;

import land.metadefi.model.Auth;
import land.metadefi.repository.UserRepository;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

@GraphQLApi
public class AuthResource {

    @Inject
    UserRepository repository;

    @Inject
    @Claim(standard = Claims.address)
    String address;

    @Query("loginWithPassword")
    @Description("Login and retrieve JWT token")
    public Auth loginWithPassword(String username, String password) {
        return repository.authWithPassword(username, password);
    }

    @Query("loginWithMetamask")
    @Description("Login with Metamask token")
    public Auth loginWithMetamask(String address, String message, String signature) {
        return repository.authWithMetamaskSignature(address, message, signature);
    }

    @Query("renewToken")
    @Description("Retrieve the new JWT token using refresh token")
    @RolesAllowed({ "User" })
    public Auth renewToken() {
        return repository.renewToken(address);
    }
}
