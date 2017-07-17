package com.example.devinlozada.chat;

/**
 * Created by Administrator on 17-05-2017.
 */

public class Show_Chat_Activity_Group_Items {

    private String GroupImage;
    private String GroupName;



    public Show_Chat_Activity_Group_Items() {}

    public Show_Chat_Activity_Group_Items(String GroupImage, String GroupName) {
        this.GroupImage    = GroupImage;
        this.GroupName   = GroupName;

    }

    public String getImageGroup() {
        return GroupImage;
    }

    public void setGroupImage(String GroupImage) {
        this.GroupImage = GroupImage;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }








}
