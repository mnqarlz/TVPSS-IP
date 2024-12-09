package com.tvpss.model;
import com.tvpss.enums.Role;

public class User {
	private String userID;
	private String name;
	private String email;
	private Role role;
	private String state;
	private String district;
	private String password;
	
	public User(String userID, String name, String email, Role role, String state, String district, String password) {
		this.userID=userID;
		this.name=name;
		this.email=email;
		this.role=role;
		this.state=state;
		this.district=district;
		this.password=password;
	}
	
	public String getUserID() {
        return userID;
    }

    public void setId(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
