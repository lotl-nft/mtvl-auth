package land.metadefi.enumrable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum UserStatus {
    ACTIVE(0),
    INACTIVE(1),
    BLOCKED(2),
    ;

    private static final Map<Integer, UserStatus> ENUM_MAP;

    static {
        Map<Integer, UserStatus> map = new ConcurrentHashMap<>();
        for (UserStatus status : UserStatus.values()) {
            map.put(status.getStatus(), status);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final Integer status;

    UserStatus(Integer status) {
        this.status = status;
    }

    public static UserStatus get(Integer status) {
        return ENUM_MAP.get(status);
    }

    public Integer getStatus() {
        return this.status;
    }
}
