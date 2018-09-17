package cic.du.ac.in.odonates;

/**
 * Created by Scorpion on 3/17/2018.
 */

public class dataFetch {
    private String cname;
    private String sname;
    private String content;
    private Long Count;

    public dataFetch() {
    }

    public dataFetch(String c, String s, String content, Long count) {
        cname = c;
        sname = s;
        content = content;
        Count = count;
    }

    public String getCname() {
        return cname;
    }

    public String getSname() {
        return sname;
    }

    public String getContent() {
        return content;
    }

    public Long getCount() {
        return Count;
    }
}
