package sk.tomas.erp.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.bo.Legal;
import sk.tomas.erp.entity.LegalEntity;
import sk.tomas.erp.repository.LegalRepository;
import sk.tomas.erp.service.LegalService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
public class LegalServiceImpl implements LegalService {

    private ModelMapper mapper;
    private LegalRepository legalRepository;

    @Autowired
    public LegalServiceImpl(ModelMapper mapper, LegalRepository legalRepository) {
        this.mapper = mapper;
        this.legalRepository = legalRepository;
    }

    @Override
    public Legal get(UUID uuid) {
        return legalRepository.findById(uuid)
                .map(legalEntity -> mapper.map(legalEntity, Legal.class))
                .orElseThrow(() -> new ResourceNotFoundException(Legal.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    public List<Legal> all() {
        List<LegalEntity> all = legalRepository.findAll();
        Type listType = new TypeToken<List<Legal>>() {
        }.getType();
        return mapper.map(all, listType);
    }

}
