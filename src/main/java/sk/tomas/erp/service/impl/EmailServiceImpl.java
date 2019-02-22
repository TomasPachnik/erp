package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.entity.AuditEntity;
import sk.tomas.erp.exception.EmailException;
import sk.tomas.erp.service.DateService;
import sk.tomas.erp.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static sk.tomas.erp.util.Utils.zip;

@Slf4j
@Service
@MethodCallLogger
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;
    private DateService dateService;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, DateService dateService) {
        this.emailSender = emailSender;
        this.dateService = dateService;
    }

    @Override
    public void sendAuditData(String toEmailAddress, List<AuditEntity> auditEntities) {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmailAddress);
            helper.setSubject("Audit data from " + dateService.getActualDate());
            helper.setText("Audit data in attachment.");
            byte[] zip = zip(list);
            ByteArrayDataSource attachment = new ByteArrayDataSource(new ByteArrayInputStream(zip), "application/octet-stream");
            helper.addAttachment("auditData.zip", attachment);
        } catch (MessagingException | IOException e) {
            log.error("Email send exception:", e);
            throw new EmailException("Email was not sent!");
        }
        emailSender.send(message);
    }
}
