package sk.tomas.erp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.repository.UserRepository;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.util.Utils;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuditService auditService;

    @Value("${admin.default.password:password}")
    private String password;

    @Autowired
    public DataInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository, AuditService auditService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Optional<UserEntity> admin = userRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            UserEntity save = userRepository.save(UserEntity.builder()
                    .username("admin")
                    .name("admin")
                    .email("admin@admin")
                    .password(this.passwordEncoder.encode(password))
                    .enabled(true)
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );
            auditService.log(UserEntity.class, save.getUuid(), null, Utils.toJson(save, InvoiceEntity.class));
        }
    }
}
