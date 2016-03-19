package com.autonavi.mapart.entity;

public enum Role {
	checker(1), tleader(2) , staff(3);
	private int role;

	private Role(int role) {
		this.role = role;
	}

	public int getRole() {
		return role;
	}
	
}
