package com.Eriyuer.service;

import java.util.List;

import com.Eriyuer.fun_video.pojo.Bgm;

public interface BgmService{
	
	public List<Bgm> queryBgmList();
	
	public Bgm queryById(String bgmId);
}
