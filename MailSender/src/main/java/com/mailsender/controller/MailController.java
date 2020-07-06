package com.mailsender.controller;

import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.utils.UUIDs;

import com.mailsender.model.Mail;
import com.mailsender.repository.MailRepository;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MailController {

  @Autowired
  MailRepository mailRepository;
  private JavaMailSender javaMailSender;

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<Mail>> getMails(@RequestParam(required = false) String email) {
	  try {
		    List<Mail> mails = new ArrayList<Mail>();
		    mailRepository.findByEmailContaining(email).forEach(mails::add);
		    if (mails.isEmpty()) {
		      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
		    return new ResponseEntity<>(mails, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
  }

  @PostMapping("/message")
  public ResponseEntity<Mail> createMail(@RequestBody Mail mail) {
	  try {
		  Mail _mail = mailRepository.save(new Mail(UUIDs.timeBased(), mail.getEmail(), mail.getTitle(), mail.getContent(), mail.getMagicNumber()));
		    return new ResponseEntity<>(_mail, HttpStatus.CREATED);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		  }
  }

  @PostMapping("/send")
  public ResponseEntity<Mail> sendMails(@RequestParam(required = true) int magic_number) {
	  List<Mail> mailData = mailRepository.findByMagicNumber(magic_number);
	  for(int i=0; i< mailData.size();i++) {
	        MimeMessage mail = javaMailSender.createMimeMessage();
	        try {
	            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
	            helper.setTo(mailData.get(i).getEmail());
	            helper.setReplyTo("example@example.com");
	            helper.setFrom("example@example.com");
	            helper.setSubject(mailData.get(i).getTitle());
	            helper.setText(mailData.get(i).getContent(), true);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	        javaMailSender.send(mail);
	  }
	  
	  try {
		    mailRepository.deleteByMagicNumber(magic_number);
		    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		  } catch (Exception e) {
		    return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		  }
  }


}