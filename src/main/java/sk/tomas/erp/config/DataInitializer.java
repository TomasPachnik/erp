package sk.tomas.erp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Value("${admin.default.password:password}")
    private String password;

    @Autowired
    public DataInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        Optional<UserEntity> admin = userRepository.findByLogin("admin");

        if (!admin.isPresent()) {
            this.userRepository.save(UserEntity.builder()
                    .login("admin")
                    .name("admin")
                    .email("admin@admin")
                    .password(this.passwordEncoder.encode(password))
                    .enabled(true)
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );
        }
    }

}
