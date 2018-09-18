package com.Eriyuer.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Eriyuer.fun_video.util.JSONResult;
import com.Eriyuer.service.BgmService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/bgm")
@Api(value="背景音乐业务的接口",tags= {"背景音乐业务的controller"})
public class BgmController {
	
	@Autowired
	private BgmService bgmService;
	
	@ApiOperation(value="获取背景音乐列表",notes="获取背景音乐的接口")
	@PostMapping("/list")
	public JSONResult list() {
		
		return JSONResult.ok(bgmService.queryBgmList());
	}
}
