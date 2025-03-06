package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.DAO.UserDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.Helpers.EmailSender;
import com.example.prm392_project.Helpers.Validation;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private String generatedOtp;
    private long otpGeneratedTime;
    private Validation validation;
    public UserRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        validation = new Validation();
    }
    public User login(String email, String password){
        if(email == null || password == null){
            return null;
        }
        User user = userDao.login(email, password);
        return user ;
    }
    public String insertUser(User user, String confirmPassword) {
        String error = "";
        if(!user.getPassword().equals(confirmPassword)){
            error = "Password is not match";
        }
        if (validation.isValidPassword(user.getPassword()) == false) {
            error = "Password is not valid";
        }
        if(userDao.getUserByPhone(user.getMobile()) != null){
            error = "Phone is already exist";
        }
        if(userDao.getUserByEmail(user.getEmail()) != null){
            error = "Email is already exist";
        }
        if(validation.isValidPhone(user.getMobile())==false){
            error = "Phone is not valid";
        }
        if (validation.isValidEmail(user.getEmail()) == false) {
            error = "Email is not valid";
        }
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getFullName().isEmpty() || user.getMobile().isEmpty() || user.getGender().isEmpty() || user.getAddress().isEmpty() || confirmPassword.isEmpty()) {
            error = "Please Fill All Information ";
        }
        if(error.length() == 0){
            String hashpassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashpassword);
            userDao.insert(user);
            return error ="success";
        }
        return error;
    }

    public String resetPassword(String email, String newPassword, String confirmPassword) {
        String error = "";
        if(!validation.isValidPassword(newPassword)){
           return error = "Password is not valid";
        }
        if(!newPassword.equals(confirmPassword)){
            return error = "Password is not match";
        }
        userDao.resetPassword(newPassword, email);
        return error;
    }

    public String receiveOTP(String email) {
        String error = "";
        if (email.isEmpty()) {
            error = "Email can not empty";
        } else if (!Validation.isValidEmail(email)) {
            error = "Email is not valid";
        } else {
            User user = userDao.getUserByEmail(email);
            if (user == null) {
                error = "Account is not exist";
            } else {
                generatedOtp = EmailSender.generateOTP();
                otpGeneratedTime = System.currentTimeMillis();
                if (!EmailSender.sendOtpEmail(email, generatedOtp)) {
                    error = "Failed to send OTP";
                }
            }
        }
        return error;
    }

    public boolean checkOTP(String otp){
        if(otp.isEmpty()){
            return false;
        }
        if (System.currentTimeMillis() - otpGeneratedTime > 60 * 1000) {
            generatedOtp = null;
            return false;
        }
        return generatedOtp.equals(otp);
    }

    public String getGeneratedOtp() {
        return generatedOtp;
    }

    public long getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    public User getUserByID(String userID){
        return userDao.getUserByID(userID);
    }
    public void updateUser(User user){
        userDao.updateUser(user);
    }
    public List<User> searchUserAsc(String fullName, String gender){
        return userDao.searchUserAsc(fullName,gender);
    }
    public List<User> searchUserDesc(String fullName, String gender){
        return userDao.searchUserDesc(fullName,gender);
    }

}
