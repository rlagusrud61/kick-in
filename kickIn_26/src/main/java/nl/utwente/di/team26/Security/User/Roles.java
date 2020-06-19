package nl.utwente.di.team26.Security.User;

public enum Roles {
    VISITOR,
    EDITOR,
    ADMIN;

    public String getString() {
        return String.valueOf(getLevel());
    }
    public int getLevel() {
        return this.ordinal();
    }

}
