import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Md5;
import com.qiniu.util.StringMap;

public class SelfV3 {
	public static Map<String, VideoInfo> vidMap = new HashMap<>();
	public static String jsonFileStr = "/Users/duanfa/trash/result/final/videoResultAllDone.json";
	public static String hlsKeoutSqlFileyUrlStr = "/Users/duanfa/trash/result/all.sql";

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			jsonFileStr = args[0];
		}
		if (args.length > 1) {
			hlsKeoutSqlFileyUrlStr = args[1];
		}
		getVideoList(jsonFileStr,hlsKeoutSqlFileyUrlStr);
	}

	private static void getVideoList(String jsonFile,String outSqlFile) throws Exception {
		String line = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outSqlFile)));
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outSqlFile+".err")));
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			JSONObject json = JSONObject.parseObject(line);
			if(StringUtils.isNotBlank(json.getString("hlsUrl"))) {
				String sql = "update tp_school_video set video_url = '"+json.getString("key")+"',hls_url = '"+json.getString("hlsUrl")+"' ,hls_key='"+json.getString("hlsKey")+"' where id = "+json.getString("id")+";";
				bw.write(sql);
				bw.newLine();
			}else {
				String sql = "update tp_school_video set video_url = '"+json.getString("key")+"',hls_url = '"+json.getString("hlsUrl")+"' ,hls_key='"+json.getString("hlsKey")+"' where id = "+json.getString("id")+";";
				bw2.write(sql);
				bw.newLine();
				
			}
		}
		br.close();
		bw.close();
	}
}
