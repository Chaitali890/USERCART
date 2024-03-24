package com.restaurant.entity;

import com.restaurant.enums.UserRole;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="User")
@Data
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private Long id;
	private String email;
	
	private String password;
	
	private String name;
	
	private UserRole role;
	
	@Lob
	@Column(columnDefinition="longblob")
	private byte[] img;
}
