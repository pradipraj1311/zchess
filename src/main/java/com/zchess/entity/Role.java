package com.zchess.entity;

public enum Role {
        ADMIN,
            USER;
	public String getAuthority() {
		return "ROLE_"+this.name();
	}
}
