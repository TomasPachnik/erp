package sk.tomas.erp.service;

import java.util.Date;

public interface DateService {

    Date getActualDate();

    Date getFirstDayOfThisMonthLastYearDate();

    Date addMonths(Date date, int months);

}
