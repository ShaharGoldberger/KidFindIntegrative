package com.example.choresandshop;

import static com.example.choresandshop.Model.UserRoleEnum.MINIAPP_USER;

import com.example.choresandshop.Model.User;

public class CurrentUserManager {
    private static CurrentUserManager instance = null;
    private User user;

    private CurrentUserManager(){

    }
    public static CurrentUserManager getInstance() {
        if ( instance == null ){
            instance = new CurrentUserManager();
        }
        return instance;
    }

    public void setUser(User user) {

        this.user = user;
    }
    public User getUser(){
        return this.user;
    }

    public boolean isChild(){
        return this.user.getAvatar().split("#")[0].equals("child") || this.user.getRole() == MINIAPP_USER;
    }

}
