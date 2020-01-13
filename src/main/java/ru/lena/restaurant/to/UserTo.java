package ru.lena.restaurant.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.SafeHtml;
import ru.lena.restaurant.HasEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class UserTo extends BaseTo implements HasEmail, Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    @SafeHtml
    private String email;

    @NotBlank
    @Size(min = 5, max = 32, message = "length must be between 5 and 32 characters")
    private String password;

    public UserTo(Long id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
