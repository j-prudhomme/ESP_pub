package Room8.Back.user.model.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDto {
    private String token;
    private UserDto user;

    public AuthenticationResponseDto(String token, Long id, String fullName, String email) {
        this.token = token;
        this.user = new UserDto(id, fullName, email);
    }
}
 