package ua.od.cepuii.library.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime dateTime;
    private boolean enabled;
    private int fine;

    public User(int id, String name, String email, String password, LocalDateTime dateTime,
                boolean enabled, int fine) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateTime = dateTime;
        this.enabled = enabled;
        this.fine = fine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (enabled != user.enabled) return false;
        if (fine != user.fine) return false;
        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(password, user.password)) return false;
        return Objects.equals(dateTime, user.dateTime);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + fine;
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateTime=" + dateTime +
                ", enabled=" + enabled +
                ", fine=" + fine +
                '}';
    }
}
