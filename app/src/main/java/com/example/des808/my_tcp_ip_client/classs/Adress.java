package com.example.des808.my_tcp_ip_client.classs;

public class Adress {
    private static  String name;
    private static  String ip;
    private static  String port;

    public static String getPort() {
        return port;
    }

    public static  void setPort(String port) {
        Adress.port = port;
    }

    public static  String getIp() {
        return ip;
    }

    public static  void setIp(String ip) {
        Adress.ip = ip;
    }

    public static  String getName() {
        return name;
    }

    public static  void setName(String name) {
        Adress.name = name;
    }

    public   Adress(String name, String ip, String port) {
        Adress.name = name;
        Adress.ip = ip;
        Adress.port = port;
    }
}
