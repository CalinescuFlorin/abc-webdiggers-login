package web_diggers.abc_backend.security.user.model;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    ARCHAEOLOGIST("ROLE_ARCHAEOLOGIST"),
    BIOLOGIST("ROLE_BIOLOGIST");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }

}
