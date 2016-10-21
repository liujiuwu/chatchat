package com.chatchat.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Li
 */
public class ChatRecord implements Serializable {

    private Integer id;
    private String type;
    private String format;
    private Date time;
    private Integer from;
    private String fromNickname;
    private Integer to;
    private String toNickname;
    private String content;

    public ChatRecord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatRecord{" + "id=" + id + ", type=" + type + ", format=" + format + ", time=" + time + ", from=" + from + ", fromNickname=" + fromNickname + ", to=" + to + ", toNickname=" + toNickname + ", content=" + content + '}';
    }
}
