package com.example.des808.my_tcp_ip_client;

public class adapter_listview {

    private String textname;
    private String textipadr;
    private String textport;
    private int    ID;

    public adapter_listview(String textname,String textipadr,String textport){
        this.textname =  textname;
        this.textipadr = textipadr;
        this.textport =  textport;
    }

    public adapter_listview() {

    }

    public String getTextname()  { return textname; }
    public String getTextipadr() { return textipadr; }
    public String getTextport()  { return textport; }
    public int    getID()        { return ID; }

    public void setTextname(String val)   {textname = val;}
    public void setTextipadr(String val)  {textipadr = val;}
    public void setTextport(String val)   {textport = val;}

    public void setID(int   val)         { ID = val; }


}
