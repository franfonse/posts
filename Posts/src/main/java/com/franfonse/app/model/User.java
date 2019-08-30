package com.franfonse.app.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity(name = "user")
public class User implements UserDetails {
	
	// Attributes
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long iduser;
	private String username;
	private String password;
	private String email;
	private Date date;
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	List<Post> listPosts;
	@ElementCollection
	@CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "roles")
	List<GrantedAuthority> roles;
	
	// Contructors


	public User() {
		
	}
	
	public User(String username, String password, String email, Date date) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.date = date;
	}
	

	// Getters & setters
	
	public long getIduser() {
		return iduser;
	}
	public void setIduser(long iduser) {
		this.iduser = iduser;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Post> getListPosts() {
		return listPosts;
	}
	public void setListPosts(List<Post> listPosts) {
		this.listPosts = listPosts;
	}
	public List<GrantedAuthority> getRoles() {
		return roles;
	}
	public void setRoles(List<GrantedAuthority> roles) {
		this.roles = roles;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}