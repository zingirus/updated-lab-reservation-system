public class Employee extends Account {
    private String role;

    public Employee(String name, int id, int password, String role) {
        super(name, id, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role + ". " + super.toString();
    }
}
