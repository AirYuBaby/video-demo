package com.Eriyuer.fun_video.pojo;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "`search_record`")
public class SearchRecord implements Serializable {
    private String id;

    private String content;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}