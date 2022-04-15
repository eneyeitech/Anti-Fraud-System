package antifraud.utility;

public enum Role {
    ADMINISTRATOR,
    MERCHANT,
    SUPPORT;

    public String withPrefix() {
        return "ROLE_" + name();
    }
}
