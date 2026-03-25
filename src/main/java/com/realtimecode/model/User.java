package com.realtimecode.model;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  public User(){}

  public User (String email, String password){
    this.email = email;
    this.password = password;
  }

  public Long getId(){return id;}
  public String getEmail(){return email;}
  public String getPassword(){return password;}

  public void setId(Long id){this.id = id;}
  public void setIEmail(String email){this.email = email;}
  public void setId(String password){this.password = password;}


}
