package sk.tomas.erp.service.impl;

import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.service.DateService;

import java.util.Calendar;
import java.util.Date;

@Service
@MethodCallLogger
public class DateServiceImpl implements DateService {

    @Override
    public Date getActualDate() {
        return new Date();
    }

    @Override
    public Date getFirstDayOfThisMonthLastYearDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    @Override
    public Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
}
