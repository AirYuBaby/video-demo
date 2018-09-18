package com.Eriyuer.fun_video.pojo;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "`users_like_videos`")
public class UsersLikeVideos implements Serializable {
    private String id;

    private String userId;

    private String cideoId;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCideoId() {
        return cideoId;
    }

    public void setCideoId(String cideoId) {
        this.cideoId = cideoId;
    }
}