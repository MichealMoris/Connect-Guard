package com.genius.models;

public class userModel
{
   private String name ;
   private String email ;
   private String password ;
    private String mobile ;
    private String adress ;
    private String carModel ;
    private String userImage ;
    private String uId ;

    public userModel(String name, String email, String password, String mobile, String adress, String carModel, String userImage, String uId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.adress = adress;
        this.carModel = carModel;
        this.userImage = userImage;
        this.uId = uId;
    }

    public userModel()
    {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
