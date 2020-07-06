package com.mailsender.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import com.mailsender.model.Mail;

public interface MailRepository extends CassandraRepository<Mail, UUID> {
  @AllowFiltering
  List<Mail> findByEmailContaining(String email);
  List<Mail> findByMagicNumber(int magic_number);
  void deleteByMagicNumber(int magic_number);
}