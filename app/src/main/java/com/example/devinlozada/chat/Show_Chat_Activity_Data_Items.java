package com.example.devinlozada.chat;

/**
 * Created by Administrator on 17-05-2017.
 */

public class Show_Chat_Activity_Data_Items {
    private String Email;
    private String image_Url;
    private String Name;
    private String isOnline;

    public Show_Chat_Activity_Data_Items() {}

    public Show_Chat_Activity_Data_Items(String email, String image_Url, String name) {
        this.Email      = email;
        this.image_Url  = image_Url;
        this.Name       = name;

    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
