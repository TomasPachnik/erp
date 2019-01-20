package sk.tomas.erp.service.impl;

import org.springframework.stereotype.Service;
import sk.tomas.erp.service.DateService;

import java.util.Date;

@Service
public class DateServiceImpl implements DateService {

    @Override
    public Date getActualDate() {
        return new Date();
    }
}
