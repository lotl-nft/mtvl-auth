package land.metadefi.utils;

import io.quarkus.test.junit.QuarkusTest;
import land.metadefi.utils.Web3Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class Web3UtilsTest {

    @Test
    void testRecoveryAddressFromSignature() {
        String address = "0x15e48f1e1239d242e751fd74b05134c01eccef9e";
        String message = "Please sign me in!";
        String signature = "0xe7ce8d4e89d22c430ecf06aaab470fafa8f4291db5335ddf13f5e7df09d2de0d3d8e3ae460c732f554efb39bc0109e7d7e3057a705c59f462a63968d68f2e9381c";

        String recoveredAddress = Web3Utils.recoverAddressFromSignature(address, message, signature);
        assertEquals(address, recoveredAddress);
    }
}
