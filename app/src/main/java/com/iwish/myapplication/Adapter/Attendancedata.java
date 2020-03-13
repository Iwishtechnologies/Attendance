package com.iwish.myapplication.Adapter;

public class Attendancedata  {

    String name,id,photo;
    Boolean state;

    public  Attendancedata(String id,String name, String photo,Boolean aBoolean)
    {
        this.id=id;
        this.name=name;
        this.photo=photo;
        this.state=aBoolean;

    }

    public void setName(String date)
    {
        this.name=date;
    }

    public void setId(String amt)
    {
        this.id=amt;
    }
    public void setPhoto(String photo)
    {
        this.photo=photo;
    }

    public void setState(Boolean aBoolean)
    {
        this.state=aBoolean;
    }


    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }
    public String getPhoto()
    {
        return photo;
    }

    public Boolean getState()
    {
        return state;
    }

}
