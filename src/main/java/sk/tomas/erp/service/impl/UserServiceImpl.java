package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.UsersRepository;
import sk.tomas.erp.service.UserService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private ModelMapper mapper;
    private UsersRepository usersRepository;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UsersRepository usersRepository) {
        this.mapper = mapper;
        this.usersRepository = usersRepository;
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
            return usersRepository.save(mapper.map(user, UserEntity.class)).getUuid();
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
}
