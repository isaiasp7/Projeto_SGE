package P_api.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String usuario;
    private String senha;
    private String perfil;
}

