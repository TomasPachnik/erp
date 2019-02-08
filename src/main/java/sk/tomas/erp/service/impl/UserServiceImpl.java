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
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.ChangeUser;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.exception.ValidationException;
import sk.tomas.erp.repository.UserRepository;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.service.UserService;
import sk.tomas.erp.util.Utils;
import sk.tomas.erp.validator.UserServiceValidator;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static sk.tomas.erp.util.Utils.createdUpdated;
import static sk.tomas.erp.validator.BaseValidator.validateUuid;
import static sk.tomas.erp.validator.UserServiceValidator.*;

@Slf4j
@Service
@MethodCallLogger
public class UserServiceImpl implements UserService {

    private ModelMapper mapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuditService auditService;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, AuditService auditService) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    @Override
    public List<User> all() {
        List<UserEntity> all = userRepository.findAll();
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    @Transactional
    public UUID save(User user) {
        validateInput(user);
        UserEntity oldUser = null;
        String userEntityOldString = null;
        try {
            UserEntity userEntity = mapper.map(user, UserEntity.class);
            if (user.getUuid() != null) {
                Optional<UserEntity> byId = userRepository.findById(user.getUuid());
                if (byId.isPresent()) {
                    oldUser = byId.get();
                    userEntityOldString = Utils.toJson(oldUser, UserEntity.class);
                    if ("admin".equals(byId.get().getUsername()) && !"admin".equals(user.getUsername())) {
                        throw new ValidationException("Admin username can not be changed!");
                    }

                    if ("admin".equals(byId.get().getUsername()) && !user.isEnabled()) {
                        throw new ValidationException("Admin account can not be disabled!");
                    }
                } else {
                    throw new ResourceNotFoundException("User can not be updated!");
                }
                byId.ifPresent(userEntity1 -> userEntity.setPassword(userEntity1.getPassword()));
                byId.ifPresent(userEntity1 -> userEntity.setRoles(userEntity1.getRoles()));
            } else {
                userEntity.setPassword(passwordEncoder.encode(user.getUsername() + "!!!"));
                userEntity.setRoles(Collections.singletonList("ROLE_USER"));
            }

            UUID uuid = userRepository.save(userEntity).getUuid();
            String userEntityNewString = Utils.toJson(userRepository.getOne(uuid), UserEntity.class);
            auditService.log(UserEntity.class, getLoggedUser().getUuid(), userEntityOldString, userEntityNewString);
            log.info(MessageFormat.format("User ''{0}'' was {1} by ''{2}''.", user.getUsername(), createdUpdated(oldUser), getByToken().getUsername()));
            return uuid;
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save user");
        }
    }

    @Override
    public User get(UUID uuid) {
        validateUuid(uuid);
        return userRepository.findById(uuid)
                .map(userEntity -> mapper.map(userEntity, User.class))
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    @Transactional
    public boolean delete(UUID uuid) {
        validateUuid(uuid);
        try {
            User user = get(uuid);
            String userEntityString = Utils.toJson(user, UserEntity.class);
            String login = user.getUsername();
            auditService.log(UserEntity.class, getLoggedUser().getUuid(), userEntityString, null);
            userRepository.deleteById(uuid);
            log.info(MessageFormat.format("User ''{0}'' was deleted by ''{1}''.", login, getLoggedUser().getUsername()));
            return true;
        } catch (EmptyResultDataAccessException | EntityNotFoundException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            List<UserEntity> userEntities = userRepository.find(currentUserName);
            if (userEntities.size() == 1) {
                return userEntities.get(0);
            }
        }
        throw new ResourceNotFoundException("Logged user not found!");
    }

    @Override
    @Transactional
    public Result changePassword(ChangePassword changePassword) {
        validatePassword(changePassword);
        UserEntity loggedUser = getLoggedUser();
        String loggedUserOldString = Utils.toJson(loggedUser, UserEntity.class);
        if (passwordEncoder.matches(changePassword.getOldPassword(), loggedUser.getPassword())) {
            loggedUser.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userRepository.save(loggedUser);
            log.info(MessageFormat.format("User ''{0}'' successfully changed password.", loggedUser.getUsername()));
            String loggedUserNewString = Utils.toJson(userRepository.getOne(loggedUser.getUuid()), UserEntity.class);
            auditService.log(UserEntity.class, loggedUser.getUuid(), loggedUserOldString, loggedUserNewString);
            return new Result(true);
        }
        log.info(MessageFormat.format("User ''{0}'' tried to change password, but was not successful.", loggedUser.getUsername()));
        return new Result(false);
    }

    @Override
    public User getByLogin(String login) {
        validateLogin(login);
        List<UserEntity> userEntities = userRepository.find(login);
        if (userEntities.size() == 1) {
            return mapper.map(userEntities.get(0), User.class);
        }
        throw new ResourceNotFoundException("User not found");
    }

    @Override
    public User getByToken() {
        return mapper.map(getLoggedUser(), User.class);
    }

    @Override
    @Transactional
    public UUID saveCurrent(ChangeUser changeUser) {
        UserServiceValidator.validateInput(changeUser);
        User byToken = getByToken();
        byToken.setName(changeUser.getName());
        byToken.setEmail(changeUser.getEmail());
        byToken.setPhone(changeUser.getPhone());
        return save(byToken);
    }

}
