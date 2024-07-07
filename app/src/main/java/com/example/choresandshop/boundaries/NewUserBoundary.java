package com.example.choresandshop.boundaries;

import com.example.choresandshop.Model.UserRoleEnum;

public class NewUserBoundary {
	
	private String email;
	private UserRoleEnum role;
	private String username;
	private String avatar;
	
	public NewUserBoundary() {
	}

	public String getEmail() {
		return email;
	}

    public void setEmail(String email) {
    	this.email = email;        
    }
	
	public UserRoleEnum getRole() {
		return role;
	}
	
	public void setRole(UserRoleEnum role) {
		this.role = role;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "NewUserBoundary [email=" + email + ", role=" + role + ", userName=" + username + ", avatar=" + avatar
				+ "]";
	}
}
