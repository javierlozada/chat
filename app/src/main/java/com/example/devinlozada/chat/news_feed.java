package com.example.devinlozada.chat;

/**
 * Created by developer on 30/06/2017.
 */

public class news_feed {
    private String profileNamePhoto;
    private String name;
    private String image;
    private String comentario;
    private String horaPublicacion;

    public news_feed(){

    }

    public news_feed(String profileNamePhoto, String name, String image, String comentario,String horaPublicacion) {
        this.profileNamePhoto = profileNamePhoto;
        this.name = name;
        this.image = image;
        this.comentario = comentario;
        this.horaPublicacion = horaPublicacion;
    }

    public String getHoraPublicacion(){
        return horaPublicacion;
    }

    public void setHoraPublicacion(String horaPublicacion){
        this.horaPublicacion = horaPublicacion;
    }

    public String getProfileNamePhoto() {
        return profileNamePhoto;
    }

    public void setProfileNamePhoto(String profileNamePhoto) {
        this.profileNamePhoto = profileNamePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


}
