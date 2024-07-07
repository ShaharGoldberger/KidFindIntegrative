package com.example.choresandshop.boundaries;

import com.example.choresandshop.Model.UserId;
import com.example.choresandshop.Model.UserRoleEnum;


public class UserBoundary {
	
	private UserId userId;
	private String username;
	private String avatar;
	private UserRoleEnum role;
	
	public UserBoundary() {
	}

	public UserId getUserId() {
		return userId;
	}
	
	public void setUserId(UserId id) {
		this.userId = id;
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
		return "UserBoundary [id=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar + "]";
	}
}
