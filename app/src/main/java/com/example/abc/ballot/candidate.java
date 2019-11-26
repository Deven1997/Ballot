package com.example.abc.ballot;

public class candidate {
    String election_id, pname, pdisc, candidate_disc, uid,mImageURL,stud_name;

    public candidate() {
        //Empty constructor needed
    }

    public candidate(String election_id, String pname, String pdisc, String candidate_disc, String uid, String mImageURL, String stud_name) {
        this.election_id = election_id;
        this.pname = pname;
        this.pdisc = pdisc;
        this.candidate_disc = candidate_disc;
        this.uid = uid;
        this.mImageURL = mImageURL;
        this.stud_name = stud_name;
    }

    //getters

    public String getElection_id() {
        return election_id;
    }

    public String getPname() {
        return pname;
    }

    public String getPdisc() {
        return pdisc;
    }

    public String getCandidate_disc() {
        return candidate_disc;
    }

    public String getUid() {
        return uid;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public String getStud_name() {
        return stud_name;
    }


//setters


    public void setElection_id(String election_id) {
        this.election_id = election_id;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPdisc(String pdisc) {
        this.pdisc = pdisc;
    }

    public void setCandidate_disc(String candidate_disc) {
        this.candidate_disc = candidate_disc;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }
}
