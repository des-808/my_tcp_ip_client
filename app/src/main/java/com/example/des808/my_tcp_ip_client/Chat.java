package com.example.des808.my_tcp_ip_client;

public class Chat {
    private int id;
    private String table_name = "";

    public int getId() {
        return id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Chat(int id,String messageIn, String dataInMessage, String messageOut, String dataOutMessage) {
        this.id = id;
        this.message_in = messageIn;
        this.data_in_message = dataInMessage;
        this.message_out = messageOut;
        this.data_out_message = dataOutMessage;
    }

    public Chat(String messageIn, String dataInMessage, String messageOut, String dataOutMessage) {
        this.message_in = messageIn;
        this.data_in_message = dataInMessage;
        this.message_out = messageOut;
        this.data_out_message = dataOutMessage;
    }

    public Chat(String messageIn, String dataInMessage) {
        this.message_in = messageIn;
        this.data_in_message = dataInMessage;
    }

    public Chat(String table_name) {
        setTable_name(table_name);
    }


        private String message_in = "";
        private String data_in_message = "";

        public String getMessage_in() {
            return message_in;
        }

        public void setMessage_in(String msg_in) {
            message_in = msg_in;
        }

        public String getData_in_message() {
            return data_in_message;
        }

        public void setData_in_message(String data_in_msg) {
            data_in_message = data_in_msg;
        }




        private String message_out = "";
        private String data_out_message = "";

        public String getMessage_out() {
            return message_out;
        }

        public void setMessage_out(String msg_out) {
            message_out = msg_out;
        }

        public String getData_out_message() {
            return data_out_message;
        }

        public void setData_out_message(String data_out_msg) {
            data_out_message = data_out_msg;
        }




}


