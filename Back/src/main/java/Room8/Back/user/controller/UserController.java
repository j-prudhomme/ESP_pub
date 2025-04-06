package Room8.Back.user.controller;

import Room8.Back.user.model.dto.UserInformation;
import Room8.Back.user.model.dto.UserMail;
import Room8.Back.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/send-email-recover-password")
    public void forgotPassword(@RequestBody UserMail object) {
        userService.forgotPassword(object);
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        return userService.resetPassword(token, newPassword, confirmPassword);
    }

    @PutMapping("/reset-password-profile")
    public String resetPasswordProfile(@RequestParam String newPassword, @RequestParam String confirmPassword) {
        return userService.resetPasswordProfile(newPassword, confirmPassword);
    }

    @GetMapping("/information")
    public UserInformation userInformation() {
        return userService.userInformation();
    }
}
