package com.tina.peoplelogger;

class PLog {

    //private variables
    private int _id;
    private String _datetime;
    private String _group;
    private String _sex;
    private String _age;
    private String _note;

    // Empty constructor
    public PLog(){

    }

    // constructor
    public PLog(int id, String datetime, String _group, String _sex, String _age, String _note){
        this._id = id;
        this._datetime = datetime;
        this._group = _group;
        this._sex = _sex;
        this._age = _age;
        this._note = _note;
    }

    // constructor
    public PLog(String datetime, String _group, String _sex, String _age, String _note){
        this._datetime = datetime;
        this._group = _group;
        this._sex = _sex;
        this._age = _age;
        this._note = _note;
    }

    // get ID
    public int getID(){ return this._id; }
    // set ID
    public void setID(int id){ this._id = id; }

    // get datetime
    public String getDatetime() { return this._datetime; }
    // set datetime
    public void setDatetime(String datetime) { this._datetime = datetime; }

    // get group
    public String getGroup() { return this._group; }
    // set group
    public void setGroup(String group){
        this._group = group;
    }

    // get gender
    public String getSex(){
        return this._sex;
    }
    // set gender
    public void setSex(String sex){
        this._sex = sex;
    }

    // get age
    public String getAge(){
        return this._age;
    }
    // set age
    public void setAge(String age){
        this._age = age;
    }

    // get note
    public String getNote(){
        return this._note;
    }
    // set note
    public void setNote(String note){ this._note = note; }

}
