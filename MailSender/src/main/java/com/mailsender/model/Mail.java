package com.mailsender.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Mail {
  @PrimaryKey
  private UUID id;

  private String email;
  private String title;
  private String content;
  private int magic_number;

  public Mail() {

  }

  public Mail(UUID id, String email, String title, String content, int magic_number) {
    this.id = id;
    this.email = email;
    this.title = title;
    this.content = content;
    this.magic_number = magic_number;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
  
  public String getEmail() {
	  return email;
  }

  public void setEmail(String email) {
	  this.email = email;
  }

  public String getTitle() {
	  return title;
  }

  public void setTitle(String title) {
	  this.title = title;
  }

  public String getContent() {
	  return content;
  }

  public void setContent(String content) {
	  this.content = content;
  }

  public int getMagicNumber() {
	  return magic_number;
  }
  
  public void setMagicNumber(int magic_number) {
	  this.magic_number = magic_number;
  }

@Override
public String toString() {
	return "Mail [id=" + id + ", email=" + email + ", title=" + title + ", content=" + content + ", magic_number="
			+ magic_number + ", toString()=" + super.toString() + "]";
}
  

}