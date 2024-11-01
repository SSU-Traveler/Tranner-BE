package com.project.capstone.member.service;

import com.project.capstone.global.exception.BusinessLogicException;
import com.project.capstone.global.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
//이메일을 보내기 위한 서비스로직
public class MailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail,
                     String title,
                     String text){
        SimpleMailMessage emailForm = createEmailForm(toEmail,title,text);
        try{
            emailSender.send(emailForm);
        }catch (RuntimeException e){
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
        }
    }
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
