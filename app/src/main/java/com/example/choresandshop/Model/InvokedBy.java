package com.example.choresandshop.Model;

import com.example.choresandshop.Model.UserId;

public class InvokedBy {
	private UserId userId;

	public InvokedBy() {
	}

	public InvokedBy(UserId userId){
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId idBoundary) {
		this.userId = idBoundary;
	}

	@Override
	public String toString() {
		return "InvokedByBoundary [idBoundary=" + userId + "]";
	}
	
	
}
