package com.example.des808.my_tcp_ip_client;

public class TitleChatsItems {
    private String name;
    private String ipadr;
    private String port;
    private int    ID;

    public TitleChatsItems(String name, String ipadr, String port){
        this.name =  name;
        this.ipadr = ipadr;
        this.port =  port;
    }

    public TitleChatsItems() {}

    public String getName()  { return name; }
    public String getIp_adr() { return ipadr; }
    public String getPort()  { return port; }
    public int    getID()        { return ID; }

    public void setName(String val)   {name = val;}
    public void setIp_adr(String val)  {ipadr = val;}
    public void setPort(String val)   {port = val;}

    public void setID(int   val)         { ID = val; }


}