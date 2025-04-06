package Room8.Back.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Room8.Back.user.model.User;
import Room8.Back.user.model.dto.LoginRequestDto;
import Room8.Back.user.model.dto.RegisterRequestDto;
import Room8.Back.user.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User register(RegisterRequestDto input) {
        try {
            User user = new User(input.getFullName(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
            userRepository.save(user);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
            return userRepository.findByEmail(input.getEmail()).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Error registering user");
        }
    }

    public User login(LoginRequestDto input) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
            return userRepository.findByEmail(input.getEmail()).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public String logout(HttpServletResponse response) {
        // Supprime le cookie contenant le token JWT
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "User logged out successfully";
    }

}