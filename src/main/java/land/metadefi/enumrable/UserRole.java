package land.metadefi.enumrable;

public enum UserRole {
    USER("User"),
    ;

    final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return this.role;
    }
}
