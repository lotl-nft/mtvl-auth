package land.metadefi.error;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("user-inactive")
public class UserInactiveException extends RuntimeException {

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
