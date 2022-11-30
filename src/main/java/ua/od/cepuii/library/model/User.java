package ua.od.cepuii.library.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User extends AbstractEntity {
    private String email;
    private String password;
    private LocalDateTime dateTime;
    private boolean enabled;
    private int fine;

    private Role role;

    public User(long id, String email, String password, LocalDateTime dateTime,
                boolean enabled, int fine, Role role) {
        super(id);
        this.email = email;
        this.password = password;
        this.dateTime = dateTime;
        this.enabled = enabled;
        this.fine = fine;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (enabled != user.enabled) return false;
        if (fine != user.fine) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(dateTime, user.dateTime)) return false;
        return role == user.role;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + fine;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateTime=" + dateTime +
                ", enabled=" + enabled +
                ", fine=" + fine +
                '}';
    }
}
