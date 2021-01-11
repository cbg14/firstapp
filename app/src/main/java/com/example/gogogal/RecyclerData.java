package com.example.gogogal;

public class RecyclerData {

    private String plan_name;
    private int progr;

    public int getProgr() {
        return progr;
    }

    public void setProgr(int progr) {
        this.progr = progr;
    }

    //생산자 construct
    public RecyclerData(String plan_name, int progr) {
        this.plan_name = plan_name;
        this.progr = progr;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }


}
