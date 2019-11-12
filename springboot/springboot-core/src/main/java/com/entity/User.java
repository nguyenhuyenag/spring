package com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/*-
	ApplicationContext context = SpringApplication.run(LearnApplication.class, args);
	UserRepository userRepository = context.getBean(UserRepository.class);
	userRepository.findAll().forEach(System.out::println);
*/
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "t_user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstname;
	private String lastname;

	@Column(name = "email_address")
	private String emailAddress;

	@UpdateTimestamp
	@Column(name = "execute_time")
	private Date executeTime;

}