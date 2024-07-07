package com.example.choresandshop.Model;

public class TargetObject {
	private ObjectId objectId;
	
	
	public TargetObject(ObjectId objectId) {
		this.objectId = objectId;
	}

	public TargetObject(){

	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	@Override
	public String toString() {
		return "TargetObject [objectId=" + objectId + "]";
	}

}
