package com.Controller;

import com.dao.UserSQL;
import com.model.User;

import java.util.ArrayList;

public class UsersController {
    private ArrayList<User> users = UserSQL.loadUsers();

    public User getUser(String username){
        for(User u : users){
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers(){
        return this.users;
    }
}
