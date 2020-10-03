package com.spring.reddit.clone.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.spring.reddit.clone.dto.NotificationEmail;
import com.spring.reddit.clone.exception.SpringRedditException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

	private final MailContentBuilder mailContentBuilder;
	private final JavaMailSender mailSender;

	@Async
	public void sendMail(NotificationEmail notificationEMail) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("bachouche.skander@gmail.com");
			messageHelper.setTo(notificationEMail.getReceipient());
			messageHelper.setSubject(notificationEMail.getSubject());
			messageHelper.setText(mailContentBuilder.build(notificationEMail.getBody()));
		};

		try {
			mailSender.send(messagePreparator);
			log.info("Activation email sent !! ");

		} catch (MailException e) {
			throw new SpringRedditException(
					"Exception occured when sending mail to " + notificationEMail.getReceipient());
		}
	}
}
