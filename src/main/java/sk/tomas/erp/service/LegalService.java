package sk.tomas.erp.service;

import sk.tomas.erp.bo.Legal;

import java.util.List;
import java.util.UUID;

public interface LegalService {

    List<Legal> all();

    Legal get(UUID uuid);
}
