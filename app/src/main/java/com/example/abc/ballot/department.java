package com.example.abc.ballot;

public class department {
    String comps,it,extc,etrx,mca;

    public department()
    {

    }

    public department(String comps, String it, String extc, String etrx, String mca) {
        this.comps = comps;
        this.it = it;
        this.extc = extc;
        this.etrx = etrx;
        this.mca = mca;
    }

    public String getComps() {
        return comps;
    }

    public String getIt() {
        return it;
    }

    public String getExtc() {
        return extc;
    }

    public String getEtrx() {
        return etrx;
    }

    public String getMca() {
        return mca;
    }
}

