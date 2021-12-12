package land.metadefi.enumrable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum UserStatus {
    ACTIVE(0),
    INACTIVE(1),
    BLOCKED(2),
    ;

    private final Integer status;

    public Integer getStatus() { return this.status; }

    private static final Map<Integer, UserStatus> ENUM_MAP;

    UserStatus(Integer status) {
        this.status = status;
    }

    static {
        Map<Integer, UserStatus> map = new ConcurrentHashMap<>();
        for (UserStatus status : UserStatus.values()) {
            map.put(status.getStatus(), status);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static UserStatus get(Integer status) {
        return ENUM_MAP.get(status);
    }
}
