package com.Eriyuer.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Eriyuer.fun_video.pojo.Users;
import com.Eriyuer.fun_video.pojo.vo.UsersVo;
import com.Eriyuer.fun_video.util.JSONResult;
import com.Eriyuer.fun_video.util.MD5Utils;
import com.Eriyuer.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户注册登陆的接口",tags= {"注册和登陆的controller"})
public class RegistLoginController extends BasicController{
	@Autowired
	private UserService userservice;
	
	@ApiOperation(value="用户注册",notes="用户注册的接口")
	@PostMapping("/regist")
	public JSONResult regist(@RequestBody Users user) throws Exception {
		//1，判断用户名和密码是否为空
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())) {
			return JSONResult.errorMsg("用户名或密码不能为空");
		}
		
		boolean usernameIsExist = userservice.queryUsernameIsExist(user.getUsername());
		
		if(!usernameIsExist) {
			user.setNickname(user.getUsername());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			user.setFansCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFollowCounts(0);
			userservice.saveUser(user);
		}else {
			return JSONResult.errorMsg("用户名已存在");
		}
		user.setPassword("");
		
//		String uniquToken = UUID.randomUUID().toString();
//		redis.set(USER_REDIS_SESSION+":"+user.getId(), uniquToken , 1000*60*30);
//		
//		UsersVo usersVo = new UsersVo();
//		BeanUtils.copyProperties(user, usersVo);
//		usersVo.setUserToken(uniquToken);
//		System.out.println(uniquToken);
		UsersVo userVo = setUserRedisSessionToken(user);
		
		return JSONResult.ok(userVo);
	}
	
	public UsersVo setUserRedisSessionToken(Users userModel) {
		String uniquToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION+":"+userModel.getId(), uniquToken , 1000*60*30);
		
		UsersVo userVo = new UsersVo();
		BeanUtils.copyProperties(userModel, userVo);
		userVo.setUserToken(uniquToken);
		return userVo;

	}
	
	@ApiOperation(value="用户登陆",notes="用户登陆的接口")
	@PostMapping("/login")
	public JSONResult login(@RequestBody Users user) throws Exception {
		 
		String username = user.getUsername();
		String password = user.getPassword();
		
		
		//1，判断用户名和密码是否为空
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
			return JSONResult.errorMsg("用户名或密码不能为空");
		}
		
		Users userResult = userservice.queryUserForLogin(username ,MD5Utils.getMD5Str(password));
		
		if(userResult!=null) {
			userResult.setPassword("");
			UsersVo userVo = setUserRedisSessionToken(userResult);
			return JSONResult.ok(userVo);
		}else {
			return JSONResult.errorMsg("用户名或密码不正确");
		}
	}
	@ApiOperation(value="用户注销",notes="用户注销的接口")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@PostMapping("/logout")
	public JSONResult logout(String userId) throws Exception {
		redis.del(USER_REDIS_SESSION+":"+userId);
		 
			return JSONResult.ok("成功注销");
	}
	
}
