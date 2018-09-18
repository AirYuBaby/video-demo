package com.Eriyuer.service;


import com.Eriyuer.fun_video.pojo.Videos;
import com.Eriyuer.fun_video.util.PagedResult;


public interface VideoService {
	
	public String saveVideo(Videos video);
	public void updateVideo(String videoId,String coverPath);
	public PagedResult getAllVideos(Integer page,Integer pageSize);
}
