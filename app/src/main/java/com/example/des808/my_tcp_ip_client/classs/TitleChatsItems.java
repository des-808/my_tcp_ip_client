package com.example.des808.my_tcp_ip_client.classs;

public class TitleChatsItems {
    private String name;
    private String ipadr;
    private String port;
    private int    ID;
    private boolean isPortOnline;
    private boolean isIpOnline;

    public TitleChatsItems(String name, String ipadr, String port){
        this.name =  name;
        this.ipadr = ipadr;
        this.port =  port;
    }
    public TitleChatsItems() {}
    public String getName()  { return name; }

    public String getIp_adr() {
        return ipadr;
    }
    public String getPort()  { return port; }
    public int    getID()        { return ID; }
    public void setName(String val)   {name = val;}
    public void setIp_adr(String val)  {ipadr = val;}
    public void setPort(String val)   {port = val;}
    public void setID(int   val)         { ID = val; }

    public void setIpOnline(boolean b) {
        isIpOnline=b;
    }
    public boolean getIpOnline() {
        return isIpOnline;
    }
    public void setPortOnline(boolean b) {
        isPortOnline=b;
    }
    public boolean getPortOnline() {
        return isPortOnline;
    }
}
