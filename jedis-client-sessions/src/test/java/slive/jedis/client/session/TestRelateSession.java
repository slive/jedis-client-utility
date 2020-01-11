package slive.jedis.client.session;

import java.util.Date;

import slive.jedis.client.session.annotation.SessionCategory;
import slive.jedis.client.session.annotation.SessionFKey;
import slive.jedis.client.session.annotation.SessionKey;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/8
 */
public class TestRelateSession {

    @SessionKey
    private String key;

    private String value;

    private long number;

    private Date date;

    @SessionCategory
    private String type;

    @SessionFKey
    private String[] fKeys;

    @SessionFKey
    private String fKey;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getfKey() {
        return fKey;
    }

    public void setfKey(String fKey) {
        this.fKey = fKey;
    }

    public String[] getfKeys() {
        return fKeys;
    }

    public void setfKeys(String[] fKeys) {
        this.fKeys = fKeys;
    }
}
