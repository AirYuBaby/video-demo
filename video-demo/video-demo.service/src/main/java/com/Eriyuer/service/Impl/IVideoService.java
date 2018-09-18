package com.Eriyuer.service.Impl;

import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Eriyuer.fun_video.mapper.VideosMapper;
import com.Eriyuer.fun_video.mapper.VideosMapperCustom;
import com.Eriyuer.fun_video.pojo.Videos;
import com.Eriyuer.fun_video.pojo.vo.VideosVO;
import com.Eriyuer.fun_video.util.PagedResult;
import com.Eriyuer.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class IVideoService implements VideoService {
	
	@Autowired
	private VideosMapper videosMapper;
	@Autowired
	private VideosMapperCustom VideosMapperCustom ;
	@Autowired
	private Sid sid;
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {
		
		String id=sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
		return id;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {
		
		Videos video =new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);

	    videosMapper.updateByPrimaryKeySelective(video);
	}

	@Override
	public PagedResult getAllVideos(Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);
		
	    List<VideosVO> list= VideosMapperCustom.queryAllVideos();
	    
	    PageInfo<VideosVO> pageList = new PageInfo<>(list);
	    
	    PagedResult pagedResult =new PagedResult();
	    pagedResult.setPage(page);
	    pagedResult.setTotal(pageList.getPages());
	    pagedResult.setRows(list);
	    pagedResult.setRecords(pageList.getTotal());
	    
		return pagedResult;
	}

}
