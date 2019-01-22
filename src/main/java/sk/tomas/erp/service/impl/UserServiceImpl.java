package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
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
import sk.tomas.erp.bo.ChangeUser;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.exception.ValidationException;
import sk.tomas.erp.repository.UsersRepository;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.service.UserService;
import sk.tomas.erp.validator.UserServiceValidator;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static sk.tomas.erp.validator.BaseValidator.validateUuid;
import static sk.tomas.erp.validator.UserServiceValidator.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private ModelMapper mapper;
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private AuditService auditService;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UsersRepository usersRepository,
                           PasswordEncoder passwordEncoder, AuditService auditService) {
        this.mapper = mapper;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    @Override
    public List<User> all() {
        List<UserEntity> all = usersRepository.findAll();
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    @Transactional
    public UUID save(User user) {
        validateInput(user);
        UserEntity oldUser = null;
        try {
            UserEntity userEntity = mapper.map(user, UserEntity.class);

            if (user.getUuid() != null) {
                Optional<UserEntity> byId = usersRepository.findById(user.getUuid());
                if (byId.isPresent()) {
                    oldUser = (UserEntity) SerializationUtils.clone(byId.get());
                    if ("admin".equals(byId.get().getLogin()) && !"admin".equals(user.getLogin())) {
                        throw new ValidationException("Admin username can not be changed!");
                    }

                    if ("admin".equals(byId.get().getLogin()) && !user.isEnabled()) {
                        throw new ValidationException("Admin account can not be disabled!");
                    }
                } else {
                    throw new ResourceNotFoundException("User can not be updated!");
                }

                byId.ifPresent(userEntity1 -> userEntity.setPassword(userEntity1.getPassword()));
                byId.ifPresent(userEntity1 -> userEntity.setRoles(userEntity1.getRoles()));
            } else {
                userEntity.setPassword(passwordEncoder.encode(user.getLogin() + "!!!"));
                userEntity.setRoles(Collections.singletonList("ROLE_USER"));
            }

            UUID uuid = usersRepository.save(userEntity).getUuid();
            auditService.log(UserEntity.class, getLoggedUser().getUuid(), oldUser, usersRepository.getOne(uuid));
            log.info("User " + user.getLogin() + " was created/updated.");
            return uuid;
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save user");
        }
    }

    @Override
    public User get(UUID uuid) {
        validateUuid(uuid);
        return usersRepository.findById(uuid)
                .map(userEntity -> mapper.map(userEntity, User.class))
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    @Transactional
    public boolean delete(UUID uuid) {
        validateUuid(uuid);
        try {
            UserEntity user = usersRepository.getOne(uuid);
            String login = user.getLogin();
            auditService.log(UserEntity.class, getLoggedUser().getUuid(), user, null);
            usersRepository.deleteById(uuid);
            log.info("User " + login + " was deleted.");
            return true;
        } catch (EmptyResultDataAccessException | EntityNotFoundException e) {
            return false;
        }
    }

    @Override
    public UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            List<UserEntity> userEntities = usersRepository.find(currentUserName);
            if (userEntities.size() == 1) {
                return userEntities.get(0);
            }
        }
        throw new ResourceNotFoundException("Logged user not found!");
    }

    @Override
    public Result changePassword(ChangePassword changePassword) {
        validatePassword(changePassword);
        UserEntity loggedUser = getLoggedUser();
        if (passwordEncoder.matches(changePassword.getOldPassword(), loggedUser.getPassword())) {
            loggedUser.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            usersRepository.save(loggedUser);
            log.info("User " + loggedUser.getLogin() + " successfully changed password.");
            auditService.log(UserEntity.class, loggedUser.getUuid(), loggedUser, usersRepository.getOne(loggedUser.getUuid()));
            return new Result(true);
        }
        log.info("User " + loggedUser.getLogin() + " tried to change password, but was not successful.");
        return new Result(false);
    }

    @Override
    public User getByLogin(String login) {
        validateLogin(login);
        List<UserEntity> userEntities = usersRepository.find(login);
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
