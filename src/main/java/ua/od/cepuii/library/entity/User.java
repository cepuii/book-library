package ua.od.cepuii.library.entity;

import lombok.*;
import ua.od.cepuii.library.entity.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String email;
    private String password;
    private LocalDateTime dateTime;
    private boolean blocked;
    private int fine;
    private Role role;

    @Builder
    public User(long id, String email, String password, LocalDateTime dateTime,
                boolean blocked, int fine, Role role) {
        super(id);
        this.email = email;
        this.password = password;
        this.dateTime = dateTime;
        this.blocked = blocked;
        this.fine = fine;
        this.role = role;
    }

}
