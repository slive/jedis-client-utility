package slive.jedis.client.session;

import java.util.Date;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/8
 */
public class TestSession {

    private String key;

    private String value;

    private long number;

    private Date date;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
