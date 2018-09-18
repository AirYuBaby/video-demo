package com.Eriyuer.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.avro.reflect.Stringable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Eriyuer.fun_video.pojo.Users;
import com.Eriyuer.fun_video.pojo.vo.UsersVo;
import com.Eriyuer.fun_video.util.JSONResult;
import com.Eriyuer.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户相关业务的接口",tags= {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController{
	@Autowired
	private UserService userservice;

	@ApiOperation(value="用户上传头像",notes="用户上传头像的接口")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@PostMapping("/uploadFace")
	public JSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files) throws Exception {
		
		if(StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("用户id不能为空。。");
		}
		String fileSpace = "E:\\fun-video";
		String uploadPathDB = "/"+userId+"/face";
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		try {
			if(files!=null&&files.length>0) {
				
				String fileName=files[0].getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)) {
					String finalFacePath=fileSpace+uploadPathDB+"/"+fileName;
					uploadPathDB+=("/"+fileName);
					File outfile=new File(finalFacePath);
					if(outfile.getParentFile()!=null||outfile.getParentFile().isDirectory()) {
						outfile.getParentFile().mkdirs();
						
					}
					fileOutputStream=new FileOutputStream(outfile);
					inputStream=files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			}else {
				return JSONResult.errorMsg("上传出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResult.errorMsg("上传出错");
		}finally {
			if(fileOutputStream!=null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		Users user=new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userservice.undateUserInfo(user);
		
		 return JSONResult.ok(uploadPathDB);
	}
	
	@ApiOperation(value="查询用户信息",notes="查询用户信息的接口")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@PostMapping("/query")
	public JSONResult query(String userId) throws Exception{
		if(StringUtils.isBlank(userId)) {
			return JSONResult.errorMap("用户id不能为空");
		}
		Users userInfo = userservice.queryUserInfo(userId);
		UsersVo usersVo =new UsersVo();
		BeanUtils.copyProperties(userInfo, usersVo);
		return JSONResult.ok(usersVo);
		
	}
	
}
