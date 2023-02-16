package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.od.cepuii.library.entity.enums.Role;

import java.io.Serializable;
import java.util.Date;
/**
 * The UserTO class is a Transfer Object (DTO) class that represents a user in the library system.
 * It includes the user's id, email, registered date, blocked status, fine amount, and role.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTO implements Serializable {

    private static final long serialVersionUID = 1;
    private long id;
    private String email;
    private Date registered;
    private boolean blocked;
    private int fine;
    private Role role;

}
