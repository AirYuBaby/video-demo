package com.Eriyuer.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Eriyuer.fun_video.enums.VideoStatusEnum;
import com.Eriyuer.fun_video.pojo.Bgm;
import com.Eriyuer.fun_video.pojo.Videos;
import com.Eriyuer.fun_video.util.FetchVideoCover;
import com.Eriyuer.fun_video.util.JSONResult;
import com.Eriyuer.fun_video.util.MergeVideoMp3;
import com.Eriyuer.fun_video.util.PagedResult;
import com.Eriyuer.service.BgmService;
import com.Eriyuer.service.VideoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/video")
@Api(value="视频相关业务的接口",tags= {"视频相关业务的controller"})
public class VideoController  extends BasicController{
	@Autowired
	private BgmService bgmService;
	@Autowired
	private VideoService videoService;
	

	@ApiOperation(value="用户上传视频",notes="用户上传视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="bgmId",value="背景音乐ID",required=false,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoSeconds",value="背景音乐播放长度",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoWidth",value="视频宽度",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoHeight",value="视频高度",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="desc",value="视频描述",required=false,dataType="String",paramType="form")
		
	})
	
	@PostMapping(value="/upload",headers="content-type=multipart/form-data")
	public JSONResult upload(String userId,
			String bgmId,double videoSeconds, int videoWidth,int videoHeight,
			String desc,
			@ApiParam(value="短视频",required=true)MultipartFile file) throws Exception {
		
		if(StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("用户id不能为空。。");
		}
//		String fileSpace = "E:\\fun-video";
		String uploadPathDB = "/"+userId+"/video";
		String coverPathDb= "/"+userId+"/video";
		FileOutputStream fileOutputStream=null;
		String finalVideoPath="";
		InputStream inputStream=null;
		try {
			if(file!=null) {
				
				String fileName=file.getOriginalFilename();
				String fileNamePrefix = "";
				String arrayFilenameItem[] =  fileName.split("\\.");
				for (int i = 0 ; i < arrayFilenameItem.length-1 ; i ++) {
					fileNamePrefix += arrayFilenameItem[i];
				}
//				String fileNamePrefix =fileName.split(".")[0];
				if(StringUtils.isNotBlank(fileName)) {
					finalVideoPath=FILESPACE+uploadPathDB+"/"+fileName;
					uploadPathDB+=("/"+fileName);
					coverPathDb= coverPathDb+"/"+fileNamePrefix+".jpg";
					File outfile=new File(finalVideoPath);
					if(outfile.getParentFile()!=null||!outfile.getParentFile().isDirectory()) {
						outfile.getParentFile().mkdirs();
						
					}
					fileOutputStream=new FileOutputStream(outfile);
					inputStream=file.getInputStream();
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
		
		
		if(StringUtils.isNotBlank(bgmId)) {
			Bgm bgm =bgmService.queryById(bgmId);
			String mp3InputPath = FILESPACE+bgm.getPath();
			
			MergeVideoMp3 tool =new MergeVideoMp3(FFMPEG_EXE);
			
			String videoInputpath = finalVideoPath;
			
			String videoOutPutName = UUID.randomUUID().toString()+".mp4";
			
			uploadPathDB="/"+userId+"/video/"+videoOutPutName;
			finalVideoPath = FILESPACE+uploadPathDB;
			System.out.println("finalVideoPath"+finalVideoPath);
			tool.convertor(videoInputpath, mp3InputPath, videoSeconds, finalVideoPath);
		}
		
		System.out.println("uploadPathDB"+uploadPathDB);
		System.out.println("finalVideoPath"+finalVideoPath);
		
		//对视频进行截图
		FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
			videoInfo.getCover(finalVideoPath,FILESPACE+coverPathDb);

		
		//保存视频信息到数据库
		
		Videos video =new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float)videoSeconds);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoPath(uploadPathDB);
		video.setCoverPath(coverPathDb);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		video.setCreatTime(new Date());
		
		
		String videoId = videoService.saveVideo(video);
		 return JSONResult.ok(videoId);
	}
	
	
	@ApiOperation(value="上传封面",notes="上传封面的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoId",value="视频主键ID",required=true,dataType="String",paramType="form")
		
	})
	
	@PostMapping(value="/uploadCover",headers="content-type=multipart/form-data")
	public JSONResult uploadCover(String userId,String videoId,
			@ApiParam(value="视频封面",required=true)
			MultipartFile file) throws Exception {
		
		if(StringUtils.isBlank(videoId)||StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("视频主键id和用户id不能为空。。");
		}
//		String fileSpace = "E:\\fun-video";
		String uploadPathDB = "/"+userId+"/video";
		FileOutputStream fileOutputStream=null;
		String finalCoverPath="";
		InputStream inputStream=null;
		try {
			if(file!=null) {
				
				String fileName=file.getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)) {
					finalCoverPath=FILESPACE+uploadPathDB+"/"+fileName;
					uploadPathDB+=("/"+fileName);
					File outfile=new File(finalCoverPath);
					if(outfile.getParentFile()!=null|| !outfile.getParentFile().isDirectory()) {
						outfile.getParentFile().mkdirs();
						
					}
					fileOutputStream=new FileOutputStream(outfile);
					inputStream=file.getInputStream();
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
		
		System.out.println(videoId);
		System.out.println(uploadPathDB);
	    videoService.updateVideo(videoId, uploadPathDB);
		
		 return JSONResult.ok();
	}
	
	
	@PostMapping(value="/showAll")
	public JSONResult showAll(Integer page) throws Exception {
		if(page == null) {
			page = 1;
		}
		PagedResult result = videoService.getAllVideos(page, PAGE_SIZE);
		return JSONResult.ok(result);
	}
	
}
