package pkcg.service;

import pkcg.model.User;
import pkcg.model.Admin;
import pkcg.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users;
    private User currentUser;
    
    public UserService() {
        this.users = new HashMap<>();
        this.currentUser = null;
    }
    
    public void registerAdmin(String username, String password) {
        users.put(username, new Admin(username, password));
    }
    
    public void registerCustomer(String username, String password, String fullName, String email) {
        users.put(username, new Customer(username, password, fullName, email));
    }
    
    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.authenticate(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isAdmin() {
        return isLoggedIn() && currentUser instanceof Admin;
    }
}