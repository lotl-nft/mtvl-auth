package land.metadefi.error;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("credentials-invalid")
public class CredentialsInvalidException extends RuntimeException {

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
