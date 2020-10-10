import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Hex;
import com.qiniu.util.Md5;
import com.qiniu.util.StringMap;

public class SelfV2 {
	public static Map<String, VideoInfo> vidMap = new HashMap<>();
	public static String baseDir = "/Users/duanfa/trash/";
	public static String hlsKeyUrl = "http://api.robotun.cn/public/shop-up-play-check/";

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			baseDir = args[0];
		}
		if (args.length > 1) {
			hlsKeyUrl = args[1];
		}
		String csv = baseDir + "id_vid.csv";
		String saveJson = baseDir + "bbvideoResult.json";
		System.out.println("csv:" + csv + " saveJson:" + saveJson);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveJson)));
		for (VideoInfo vInfo : getVideoList(csv)) {
			try {
				VideoInfo v = upload(vInfo);
				vidMap.put(v.videoid, v);
				bw.write(v.toString());
				System.out.println(v);
			} catch (Exception e) {
				bw.write(vInfo.toString());
				e.printStackTrace();
			}
			bw.flush();
			bw.newLine();
		}
		bw.close();
	}

	public static JSONObject getJsonFromUrl(String url) {
		System.out.println(url);
		StringBuilder sb = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			// 设置编码格式 解决中文乱码问题
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		return JSONObject.parseObject(sb.toString());
	}

	public static void downLoad(String urlPath, String savePath) throws Exception {
		int bytesum = 0;
		int byteread = 0;

		URL url = new URL(urlPath);

		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(savePath);

			byte[] buffer = new byte[1204];
			int i = 0;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				if(i++%100==0) {
					System.out.println(new Date()+ " "+bytesum+"byte downloaded and still downloading...");
				}
			}
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static VideoInfo upload(VideoInfo videoInfo) throws Exception {
		System.out.println(videoInfo.id + " start:" + new Date());
		String id = videoInfo.id;
		String localFilePath = baseDir + "tmpFile";
		if (vidMap.get(videoInfo.videoid) != null) {
			VideoInfo sameVideo = vidMap.get(videoInfo.videoid);
			videoInfo.processid = sameVideo.processid;
			videoInfo.key = sameVideo.key;
			videoInfo.hlsKey = sameVideo.hlsKey;
			videoInfo.hlsUrl = sameVideo.hlsUrl;
			System.out.println(videoInfo.id + " end:" + new Date());
			return videoInfo;
		}
		File file = new File(localFilePath);
		file.delete();
		JSONObject ccInfo = getCCInfo(videoInfo.videoid);
		videoInfo.ccurl = ccInfo.getJSONObject("video").getString("url");
		String infoMd5 = ccInfo.getJSONObject("video").getString("md5").toLowerCase();
		downLoad(videoInfo.ccurl, localFilePath);
		String md5Download  = getMD5(new File(localFilePath)).toLowerCase();
		System.out.println("md5Download:"+md5Download+" infoMd5:"+infoMd5);
		if(!infoMd5.equals(md5Download)) {
			throw new Exception("download Error");
		}
		
		System.out.println("download finish! start upload!");
		// 构造一个带指定 Region 对象的配置类
		com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration();
		UploadManager uploadManager = new UploadManager(cfg);
		// ...其他参数参考类注释
		// ...生成上传凭证，然后准备上传
		String accessKey = "nYlubQo3Cffp4Opj23CZ_2VOcZQiJBEX3igCAUsE";
		String secretKey = "u1DuhOhRXdO50ebmX7tXNU8i6n5zWhw2fKOaDcqq";
		// String bucket = "static";
		String bucket = "lecv";
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
		// String localFilePath =
		// "/home/duanfa/Downloads/F1605312C7D6466D9C33DC5901307461.mp4";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		// 设置上传后的文件名称
		// String key = "F1605312C7D6466D9C33DC5901307461";
		Auth auth = Auth.create(accessKey, secretKey);
		// 上传自定义参数，自定义参数名称需要以 x:开头
		StringMap params = new StringMap();
		params.put("x:id", id);
		String uuid16 = System.currentTimeMillis() + "000";
		String hlsKey = java.util.Base64.getEncoder().encodeToString(uuid16.getBytes());
		String hlsKeyUrl = java.util.Base64.getEncoder().encodeToString((SelfV2.hlsKeyUrl + id).getBytes());
		params.put("x:hlsKey", hlsKey);
		params.put("x:hlsKeyUrl", hlsKeyUrl);
		params.put("x:age", 20);
		// 上传策略
		StringMap policy = new StringMap();
		// 自定义上传后返回内容，返回自定义参数，需要设置 x:参数名称，注意下面 persistentId
		policy.put("returnBody",
				"{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"id\":\"$(x:id)\",\"hlsKey\":\"$(x:hlsKey)\",\"hlsKeyUrl\":\"$(x:hlsKeyUrl)\",\"persistentId\":\"$(persistentId)\"}");
		policy.put("persistentOps", "avthumb/m3u8/noDomain/1/hlsKey/" + hlsKey + "/hlsKeyUrl/" + hlsKeyUrl);
		// 生成上传token
		String upToken = auth.uploadToken(bucket, null, 3600, policy);
		try {
			Response response = uploadManager.put(localFilePath, null, upToken, params, null, false);
			JSONObject json = JSONObject.parseObject(response.bodyString());
			videoInfo.processid = json.getString("persistentId");
			videoInfo.key = json.getString("key");
			videoInfo.hlsKey = uuid16;
			while (StringUtils.isBlank(videoInfo.hlsUrl)) {
				try {
					Thread.sleep(2000);
					JSONObject processJson = getJsonFromUrl(
							"http://api.qiniu.com/status/get/prefop?id=" + videoInfo.processid);
					videoInfo.hlsUrl = processJson.getJSONArray("items").getJSONObject(0).getString("key");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
			}
		}
		System.out.println(videoInfo.id + " end:" + new Date());
		return videoInfo;
	}

	private static JSONObject getCCInfo(String videoid) throws Exception {
		long time = System.currentTimeMillis() / 1000;
		String rawArgs = "userid=BDCCCDBC3050AF66&videoid=" + videoid + "&time=" + time;
		String args = rawArgs + "&salt=RxBuHAC64K1QkFYkSanycitZbR6Ibwvj";
		String hash = Md5.md5(args.getBytes());
		String url = "http://spark.bokecc.com/api/video/original?" + rawArgs + "&hash=" + hash;
		JSONObject ccJson = getJsonFromUrl(url);
		return ccJson;
	}

	private static List<VideoInfo> getVideoList(String csv) throws Exception {
		List<VideoInfo> list = new ArrayList<>();
		String line = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csv)));
		while ((line = br.readLine()) != null) {
			VideoInfo videoInfo = new VideoInfo();
			String[] fields = line.split(",");
			if (fields.length > 0) {
				videoInfo.id = fields[0];
			}
			if (fields.length > 1) {
				videoInfo.videoid = fields[1];
			}
			list.add(videoInfo);
		}
		return list;
	}
	 public static String getMD5(File file) {
	        FileInputStream fileInputStream = null;
	        try {
	            MessageDigest MD5 = MessageDigest.getInstance("MD5");
	            fileInputStream = new FileInputStream(file);
	            byte[] buffer = new byte[8192];
	            int length;
	            while ((length = fileInputStream.read(buffer)) != -1) {
	                MD5.update(buffer, 0, length);
	            }
	            return new String(Hex.encodeHex(MD5.digest()));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        } finally {
	            try {
	                if (fileInputStream != null){
	                    fileInputStream.close();
	                    }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
