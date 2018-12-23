package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.UsersRepository;
import sk.tomas.erp.service.UserService;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private ModelMapper mapper;
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> all() {
        List<UserEntity> all = usersRepository.findAll();
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public UUID save(User user) {
        try {
            UserEntity userEntity = mapper.map(user, UserEntity.class);

            if (user.getUuid() != null) {
                Optional<UserEntity> byId = usersRepository.findById(user.getUuid());
                byId.ifPresent(userEntity1 -> userEntity.setPassword(userEntity1.getPassword()));
                byId.ifPresent(userEntity1 -> userEntity.setRoles(userEntity1.getRoles()));
            } else {
                userEntity.setPassword(passwordEncoder.encode(user.getName() + "a"));
                userEntity.setRoles(Collections.singletonList("ROLE_USER"));
            }

            return usersRepository.save(userEntity).getUuid();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save user");
        }
    }

    @Override
    public User get(UUID uuid) {
        return usersRepository.findById(uuid)
                .map(userEntity -> mapper.map(userEntity, User.class))
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    public boolean delete(UUID uuid) {
        try {
            usersRepository.deleteById(uuid);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Result changePassword(ChangePassword changePassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            List<UserEntity> userEntities = usersRepository.find(currentUserName);
            if (userEntities.size() == 1) {
                UserEntity userEntity = userEntities.get(0);
                if (passwordEncoder.matches(changePassword.getOldPassword(), userEntity.getPassword())) {
                    userEntity.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                    usersRepository.save(userEntity);
                    return new Result(true);
                }
            }
        }
        return new Result(false);
    }

    @Override
    public User getByLogin(String login) {
        List<UserEntity> userEntities = usersRepository.find(login);
        if (userEntities.size() == 1) {
            return mapper.map(userEntities.get(0), User.class);
        }
        throw new ResourceNotFoundException("User not found");
    }
}
