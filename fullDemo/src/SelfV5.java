import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.qiniu.util.Md5;

public class SelfV5 {
	public static Map<String, VideoInfo> vidMap = new HashMap<>();
	public static String jsonFile = "/Users/duanfa/trash/result/videoResultMoreProcessing.json";
	public static String outSaveFile = "/Users/duanfa/trash/result/videoResultMoreDone.json";

	public static void main(String[] args) throws Exception {
		String line = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outSaveFile)));
		while ((line = br.readLine()) != null) {
			try {
				System.out.println(line);
				JSONObject json = JSONObject.parseObject(line);
				while (StringUtils.isBlank(json.getString("hlsUrl"))) {
					try {
						Thread.sleep(2000);
						JSONObject processJson = getJsonFromUrl(
								"http://api.qiniu.com/status/get/prefop?id=" + json.getString("processid"));
						json.put("hlsUrl",  processJson.getJSONArray("items").getJSONObject(0).getString("key"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				bw.write(json.toString());
				bw.newLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		br.close();
		bw.close();

	}

	private static JSONObject reProc(JSONObject json) {
		String key = json.getString("key"), id = json.getString("id");
		String accessKey = "nYlubQo3Cffp4Opj23CZ_2VOcZQiJBEX3igCAUsE";
		String secretKey = "u1DuhOhRXdO50ebmX7tXNU8i6n5zWhw2fKOaDcqq";
		String bucket = "lecv";
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = "/home/duanfa/Downloads/F1605312C7D6466D9C33DC5901307461.mp4";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名

		Auth auth = Auth.create(accessKey, secretKey);
		String uuid16 = System.currentTimeMillis() + "000";
		json.put("hlsKey", uuid16);
		String hlsKey = java.util.Base64.getEncoder().encodeToString(uuid16.getBytes());
		String hlsKeyUrl = java.util.Base64.getEncoder().encodeToString((SelfV2.hlsKeyUrl + id).getBytes());
		String fops = "avthumb/m3u8/noDomain/1/hlsKey/" + hlsKey + "/hlsKeyUrl/" + hlsKeyUrl;
		// 将多个数据处理指令拼接起来
		String persistentOpfs = StringUtils.join(new String[] { fops }, ";");
		// 数据处理队列名称，必须
		String persistentPipeline = "default.sys";
		// 数据处理完成结果通知地址
		// String persistentNotifyUrl = "http://api.example.com/qiniu/pfop/notify";
		// 构造一个带指定 Region 对象的配置类
		Configuration cfg = new Configuration(Region.region0());
		// ...其他参数参考类注释
		// 构建持久化数据处理对象
		OperationManager operationManager = new OperationManager(auth, cfg);
		try {
			String persistentId = operationManager.pfop(bucket, key, persistentOpfs, persistentPipeline, null, true);
			// 可以根据该 persistentId 查询任务处理进度
			json.put("processid", persistentId);
			OperationStatus operationStatus = operationManager.prefop(persistentId);
			// 解析 operationStatus 的结果
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
		return json;
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
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getCCurl(String videoid) throws Exception {
		long time = System.currentTimeMillis() / 1000;
		String rawArgs = "userid=BDCCCDBC3050AF66&videoid=" + videoid + "&time=" + time;
		String args = rawArgs + "&salt=RxBuHAC64K1QkFYkSanycitZbR6Ibwvj";
		String hash = Md5.md5(args.getBytes());
		String url = "http://spark.bokecc.com/api/video/original?" + rawArgs + "&hash=" + hash;
		JSONObject ccJson = getJsonFromUrl(url);
		return ccJson.getJSONObject("video").getString("url");
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
}
