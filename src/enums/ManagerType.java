package enums;

public enum ManagerType {
    OR,
    DEPARTMENT_HEAD,
    DEAN;

    @Override
    public String toString() {
        return switch (this) {
            case OR -> "Office of the Registrar";
            case DEPARTMENT_HEAD -> "Department Head";
            case DEAN -> "Dean";
        };
    }
}
