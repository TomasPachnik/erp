package sk.tomas.erp.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.UserRepository;
import sk.tomas.erp.service.UserService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private ModelMapper mapper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UserRepository userRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> all() {
        List<UserEntity> all = userRepository.findAll();
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public UUID save(User user) {
        try {
            return userRepository.save(mapper.map(user, UserEntity.class)).getUuid();
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());
            throw new SqlException("Cannot save user");
        }
    }

    @Override
    public User get(UUID uuid) {
        return userRepository.findById(uuid)
                .map(userEntity -> mapper.map(userEntity, User.class))
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    public boolean delete(UUID uuid) {
        try {
            userRepository.deleteById(uuid);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
