package com.Eriyuer.service.Impl;

import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Eriyuer.fun_video.mapper.BgmMapper;
import com.Eriyuer.fun_video.pojo.Bgm;
import com.Eriyuer.service.BgmService;
@Service
public class IBgmService implements BgmService {
	
	@Autowired
	private BgmMapper bgmMapper;
	
	@Autowired
	private Sid sid;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<Bgm> queryBgmList() {
		
		return bgmMapper.selectAll();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Bgm queryById(String bgmId) {
		return bgmMapper.selectByPrimaryKey(bgmId);
	}

}
