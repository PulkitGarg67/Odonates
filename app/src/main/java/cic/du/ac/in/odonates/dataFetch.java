package cic.du.ac.in.odonates;

/**
 * Created by Scorpion on 3/17/2018.
 */

public class dataFetch {
    private String Cname;
    private String Sname;

    public dataFetch() {
    }

    public dataFetch(String cname, String sname) {
        Cname = cname;
        Sname = sname;
    }

    public String getCname() {
        return Cname;
    }

    public String getSname() {
        return Sname;
    }
}
