package com.Eriyuer.service;

import com.Eriyuer.fun_video.pojo.Users;

public interface UserService {
	//判断用户名是否存在
	public boolean queryUsernameIsExist(String username);
	//保存用户注册信息
	public void saveUser(Users user);
	
	public Users queryUserForLogin(String username,String password);
	
	public void undateUserInfo(Users user);
	
	public Users queryUserInfo(String userId);
}
