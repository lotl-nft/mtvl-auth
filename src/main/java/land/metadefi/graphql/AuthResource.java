package land.metadefi.graphql;

import land.metadefi.model.Auth;
import land.metadefi.repository.UserRepository;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import javax.inject.Inject;

@GraphQLApi
public class AuthResource {

    @Inject
    UserRepository repository;

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
}
