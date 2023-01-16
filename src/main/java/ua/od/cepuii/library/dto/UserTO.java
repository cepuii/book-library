package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.od.cepuii.library.entity.enums.Role;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTO implements Serializable {

    private static final long serialVersionUID = 1;
    private long id;
    private String email;
    private String registered;
    private boolean enabled;
    private int fine;
    private Role role;

}
