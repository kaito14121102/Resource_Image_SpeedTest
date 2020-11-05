package com.example.crosspromotesdk.modal;
public class Data {
    private Advertisements[] list_advertisements;

    private String time_server;

    public Advertisements[] getList_advertisements() {
        return list_advertisements;
    }

    public void setList_advertisements(Advertisements[] list_advertisements) {
        this.list_advertisements = list_advertisements;
    }

    public String getTime_server() {
        return time_server;
    }

    public void setTime_server(String time_server) {
        this.time_server = time_server;
    }

    @Override
    public String toString() {
        return "ClassPojo [list_advertisements = " + list_advertisements + ", time_server = " + time_server + "]";
    }
}
