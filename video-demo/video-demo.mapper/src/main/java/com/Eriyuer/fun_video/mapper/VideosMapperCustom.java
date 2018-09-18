package com.Eriyuer.fun_video.mapper;

import java.util.List;

import com.Eriyuer.fun_video.pojo.Videos;
import com.Eriyuer.fun_video.pojo.vo.VideosVO;
import com.Eriyuer.fun_video.util.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	public List<VideosVO> queryAllVideos();
}