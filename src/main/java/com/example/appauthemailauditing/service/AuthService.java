package com.example.appauthemailauditing.service;

import com.example.appauthemailauditing.config.UserConfig;
import com.example.appauthemailauditing.entity.User;
import com.example.appauthemailauditing.entity.enums.RoleName;
import com.example.appauthemailauditing.payload.ApiResponse;
import com.example.appauthemailauditing.payload.RegisterDto;
import com.example.appauthemailauditing.repository.RoleRepository;
import com.example.appauthemailauditing.repository.UserRepository;
import com.example.appauthemailauditing.secutiry.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserConfig userConfig;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto dto) {          //login qilish uchun

        // -------- EXIST BY EMAIL -----------//
        boolean existsByEmail = userRepository.existsByEmail(dto.getEmail());
        if (existsByEmail)
            return new ApiResponse("This email already exists", false);

        //-------- ADD USER TO DATABASE -------//
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        //--------EMAIL ga XABAR YUBORISH--------//
        boolean b = sendEmail(user.getEmail(), user.getEmailCode());
        if (b)
            return new ApiResponse("Successfully registered, please verify your email", true);
        return new ApiResponse("something is error", false);

    }

    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("hojiakbarikkinchi@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Tasdiqlash kodi");
            simpleMailMessage.setText("<a href='http://localhost:8081/api/auth/verifyEmail?email=" + sendingEmail + "&emailCode=" + emailCode + "'>tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String email, String emailCode) {

        // -------  CHECK USER IS EXISTS -------------//
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isEmpty())
            return new ApiResponse("Account not verified", false);

        // -------  ENABLE USER'S ENABLE -------------//
        User user = optionalUser.get();
        user.setEnabled(true);
        user.setEmailCode(null);
        userRepository.save(user);
        return new ApiResponse("Account verified", true);
    }

    public ApiResponse login(RegisterDto dto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail(), user.getRoles());
            return new ApiResponse("token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("email or password incorrect", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }
}
