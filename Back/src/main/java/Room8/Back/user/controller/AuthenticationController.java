package Room8.Back.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Room8.Back.security.jwt.JwtService;
import Room8.Back.user.model.User;
import Room8.Back.user.model.dto.LoginRequestDto;
import Room8.Back.user.model.dto.RegisterRequestDto;
import Room8.Back.user.model.dto.AuthenticationResponseDto;
import Room8.Back.user.service.AuthenticationService;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequestDto registerUserDto) {
        User authenticatedUser = this.authenticationService.register(registerUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(new AuthenticationResponseDto(jwtToken, authenticatedUser.getId(), authenticatedUser.getFullName(), authenticatedUser.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto loginUserDto) {
        User authenticatedUser = this.authenticationService.login(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(new AuthenticationResponseDto(jwtToken, authenticatedUser.getId(), authenticatedUser.getFullName(), authenticatedUser.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        String logoutMessage = authenticationService.logout(response);
        return ResponseEntity.ok(logoutMessage);
    }
}