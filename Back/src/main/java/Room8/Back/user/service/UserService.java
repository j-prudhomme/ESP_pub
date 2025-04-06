package Room8.Back.user.service;

import Room8.Back.user.model.User;
import Room8.Back.user.model.dto.UserDto;
import Room8.Back.user.model.dto.UserInformation;
import Room8.Back.user.model.dto.UserMail;
import Room8.Back.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public UserInformation userInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = this.userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            return new UserInformation(user.get().getFullName(), user.get().getEmail());
        } else {
            return null;
        }
    }

    public void forgotPassword(UserMail object) {
        Optional<User> userOpt = userRepository.findByEmail(object.getEmail());
        if (!userOpt.isPresent()) {
            return;
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1); // Token valide 1h

        user.setResetToken(token);
        user.setTokenExpirationTime(expirationTime);
        userRepository.save(user);

        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("room8msc@gmail.com");
        message.setTo(object.getEmail());
        message.setSubject("Réinitialisation de votre mot de passe Room8");
        message.setText("Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetUrl);

        mailSender.send(message);
    }

    public String resetPassword(String token, String newPassword, String confirmPassword) {
        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (!userOpt.isPresent()) {
            return "Token invalide ou expiré";
        }

        User user = userOpt.get();

        if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            return "Token expiré";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "Les mots de passe ne correspondent pas";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));

        user.setResetToken(null);
        user.setTokenExpirationTime(null);
        userRepository.save(user);

        return "Mot de passe mis à jour avec succès";
    }

    public String resetPasswordProfile(String newPassword, String confirmPassword) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = this.userRepository.findByEmail(authentication.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!newPassword.equals(confirmPassword)) {
                return "Les mots de passe ne correspondent pas";
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return "Mot de passe mis à jour avec succès";
        } else {
            return null;
        }
    }
}
