package sk.tomas.erp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.AuthenticationRequest;
import sk.tomas.erp.config.JwtTokenProvider;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.LoginException;
import sk.tomas.erp.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/ping")
    public String ping() {
        return "ping works";
    }


    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        UserEntity userEntity = checkLogin(data);
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(userEntity, this.userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            log.info("User " + data.getUsername() + " logged in.");
            return ok(model);
        } catch (BadCredentialsException bce) {
            log.info("User " + data.getUsername() + " inserted bad credentials.");
            throw new LoginException("Bad credentials");
        } catch (DisabledException e) {
            log.info("User " + data.getUsername() + " tried logged in, but is not active.");
            throw new LoginException("User not active");
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList())
        );
        return ok(model);
    }

    private UserEntity checkLogin(AuthenticationRequest data) {
        Optional<UserEntity> byLogin = userRepository.findByLogin(data.getUsername());
        if (byLogin.isPresent()) {
            if (!passwordEncoder.matches(data.getPassword(), byLogin.get().getPassword())) {
                throw new LoginException("Bad credentials");
            }
            return byLogin.get();
        }
        return null;
    }

}
