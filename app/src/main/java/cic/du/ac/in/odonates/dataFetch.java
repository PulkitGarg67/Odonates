package cic.du.ac.in.odonates;

/**
 * Created by Scorpion on 3/17/2018.
 */

public class dataFetch {
    private String Cname;
    private String Sname;
    private String Content;
    private Long Count;

    public dataFetch() {
    }

    public dataFetch(String cname, String sname, String content, Long count) {
        Cname = cname;
        Sname = sname;
        Content = content;
        Count = count;
    }

    public String getCname() {
        return Cname;
    }

    public String getSname() {
        return Sname;
    }

    public String getContent() {
        return Content;
    }

    public Long getCount() {
        return Count;
    }
}
