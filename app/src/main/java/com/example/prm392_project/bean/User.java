package com.example.prm392_project.bean;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int UserID;
    private String FullName;
    private String Mobile;
    private String Email;
    private String Address;
    private String Gender;
    private String Password;
    private boolean IsActive;
    private boolean IsAdmin;

    // Constructor
    public User(String FullName, String Mobile, String Email, String Address, String Gender, String Password, boolean IsActive, boolean IsAdmin) {
        this.FullName = FullName;
        this.Mobile = Mobile;
        this.Email = Email;
        this.Address = Address;
        this.Gender = Gender;
        this.Password = Password;
        this.IsActive = IsActive;
        this.IsAdmin = IsAdmin;
    }

    public User() {

    }

    // Getters and Setters
    public int getUserID() { return UserID; }
    public void setUserID(int UserID) { this.UserID = UserID; }
    public String getFullName() { return FullName; }
    public void setFullName(String FullName) { this.FullName = FullName; }
    public String getMobile() { return Mobile; }
    public void setMobile(String Mobile) { this.Mobile = Mobile; }
    public String getEmail() { return Email; }
    public void setEmail(String Email) { this.Email = Email; }
    public String getAddress() { return Address; }
    public void setAddress(String Address) { this.Address = Address; }
    public String getGender() { return Gender; }
    public void setGender(String Gender) { this.Gender = Gender; }
    public String getPassword() { return Password; }
    public void setPassword(String Password) { this.Password = Password; }
    public boolean getIsActive() { return IsActive; }
    public void setIsActive(boolean IsActive) { this.IsActive = IsActive; }
    public boolean getIsAdmin() { return IsAdmin; }
    public void setIsAdmin(boolean IsAdmin) { this.IsAdmin = IsAdmin; }
}