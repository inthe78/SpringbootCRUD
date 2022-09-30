package com.inthe.model;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Data
@Entity
@Table(name="user_table", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Long id;
	
	@Column(name="email")
	@NonNull
	@Email(regexp = "^(.+)@(.+)$", message = "Invalid email")
	protected String email;
	
	@Column(name="password")
	@NonNull
	@Size(min = 4, message = "password should atleast be of lenght 4")
	protected String pass;
	
	@Column(name="name")
	@NonNull
	@Pattern(regexp = "^[a-zA-Z\\s.*]*$",message = "cannot contain numbers")
	@Size(min=1,message = "cannot be empty")
	protected String name;
	
	@Column(name = "joining_date")
	@NonNull
	@Size(min = 1,message = "Date cannot be empty")
	protected String joiningDate;
	

	@Column(name = "address")
	@NonNull
	@Size(min=1, message = "Address cannot be empty!")
	protected String address;
	
	
	@Column(name = "role")
	protected String role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Collections.singleton(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.pass;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
