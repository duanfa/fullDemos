import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;

public class SelfV2 {
	public static void main(String[] args) {
//		for(Map.Entry<String, String> entry:getCCvideo().entrySet()) {
//			upload(entry.getKey(),entry.getValue());
//		}
		upload("28","/home/duanfa/trash/aa.mp4");
	}

	public static void upload(String id,String localFilePath) {
		// 构造一个带指定 Region 对象的配置类
		System.out.println(new Date());
		com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration();
		UploadManager uploadManager = new UploadManager(cfg);
		// ...其他参数参考类注释
		// ...生成上传凭证，然后准备上传
		String accessKey = "nYlubQo3Cffp4Opj23CZ_2VOcZQiJBEX3igCAUsE";
		String secretKey = "u1DuhOhRXdO50ebmX7tXNU8i6n5zWhw2fKOaDcqq";
		String bucket = "static";
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
//		String localFilePath = "/home/duanfa/Downloads/F1605312C7D6466D9C33DC5901307461.mp4";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		// 设置上传后的文件名称
		// String key = "F1605312C7D6466D9C33DC5901307461";
		Auth auth = Auth.create(accessKey, secretKey);
		// 上传自定义参数，自定义参数名称需要以 x:开头
		StringMap params = new StringMap();
		params.put("x:id", id);
		String hlsKey = java.util.Base64.getEncoder().encodeToString("examplekey123456".getBytes());
		String hlsKeyUrl = java.util.Base64.getEncoder().encodeToString("http://ogtoywd4d.bkt.clouddn.com/hls128.key".getBytes());
		params.put("x:hlsKey", hlsKey);
		params.put("x:hlsKeyUrl", hlsKeyUrl);
		params.put("x:age", 20);
		// 上传策略
		StringMap policy = new StringMap();
		// 自定义上传后返回内容，返回自定义参数，需要设置 x:参数名称，注意下面 persistentId
		policy.put("returnBody","{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"id\":\"$(x:id)\",\"hlsKey\":\"$(x:hlsKey)\",\"hlsKeyUrl\":\"$(x:hlsKeyUrl)\",\"persistentId\":\"$(persistentId)\"}");
		policy.put("persistentOps", "avthumb/m3u8/noDomain/1/vb/500k/t/10/hlsKey/"+hlsKey+"/hlsKeyUrl/"+hlsKeyUrl);
		// 生成上传token
		String upToken = auth.uploadToken(bucket, null, 3600, policy);
		try {
			Response response = uploadManager.put(localFilePath, null, upToken, params, null, false);
			System.out.println(response.bodyString());
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
			}
		}
		System.out.println(new Date());
	}

	private static Map<String, String> getCCvideo() {
		Map<String, String> map = new HashMap<>();
		map.put("3", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-10-18/0A9E21C47FCC529F9C33DC5901307461.mp4?t=1601281227&key=5A99D4DB7D9BDBDCFF50D895C6B35CFC");
		map.put("10", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-11-15/0592681B40B55F709C33DC5901307461.wmv?t=1601281228&key=81FFE930F6524EC8F27CABBCAE422BF8");
		map.put("21", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-11-18/85C0BC0DD6EAC4E69C33DC5901307461.mp4?t=1601281228&key=8B4E07B8EA67FE030C535A3B7020955E");
		map.put("22", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-11-18/EE9A7297C1CB14599C33DC5901307461.flv?t=1601281229&key=914A7B944DBF3F1802F19575128339B0");
		map.put("23", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-11-21/3446426769A5D8419C33DC5901307461.flv?t=1601281229&key=C28AC9F3FBA304C52AE1E4FDB47E84D9");
		map.put("25", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-11-24/48034EC49D20C6E29C33DC5901307461.flv?t=1601281229&key=88F181A8D4888C1C2872057E4DC884BF");
		map.put("29", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-11/3E463F75F545B2969C33DC5901307461.flv?t=1601281229&key=E7991499AEF44A6DF64BA72CA48DD6CA");
		map.put("30", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-15/AE813AA230A0BB5C9C33DC5901307461.mp4?t=1601281229&key=9B54208DEACCE811C5775B2F03B3836D");
		map.put("31", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-15/836FFB115AEE9EBC9C33DC5901307461.mp4?t=1601281229&key=1A7FB468396469B6C568B658264FB2F9");
		map.put("32", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-15/8164DB9DBBC3391D9C33DC5901307461.mp4?t=1601281229&key=81EB0A3CD72FD207F3ECAD2E928C3F80");
		map.put("33", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-15/ED74E4F280CE69509C33DC5901307461.mp4?t=1601281230&key=E24004CDD624F3439D73EC933ABB4EAB");
		map.put("34", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-15/C79B5BAB5B7436569C33DC5901307461.mp4?t=1601281230&key=C79C09C2AB49DB4D2F989161FBFA6AC6");
		map.put("35", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-15/E39AA705947CE4E49C33DC5901307461.mp4?t=1601281230&key=CA304C885870A63C0C8B6C719678F80F");
		map.put("36", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-26/ECFE1C806BAFD2349C33DC5901307461.mpg?t=1601281230&key=40463B6735A958530F02A3DE3A8AF422");
		map.put("37", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-26/2289139CE659E9F49C33DC5901307461.mpg?t=1601281230&key=BA658F54FB358E0FFC82C7EA601880DC");
		map.put("38", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/3F9BA3643F1EAC399C33DC5901307461.mpg?t=1601281230&key=03735C680197111534A97E7AB1641BD4");
		map.put("39", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/AC4F4F0661911D8A9C33DC5901307461.mpg?t=1601281230&key=AFD968D8631ABCEE372CE915B395BE4B");
		map.put("40", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/0BE16C31E4B427469C33DC5901307461.mpg?t=1601281230&key=49CFC641ECA1F8AA72D6A431EAF1D5EA");
		map.put("41", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/811D7F1BD2D63CB79C33DC5901307461.mpg?t=1601281230&key=8276D36A3895AB808F3252EB7ADB71EE");
		map.put("42", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/24AD9AB3830EB1A29C33DC5901307461.mp4?t=1601281230&key=826F1A4336FD0A6553A1CC27297B7DF6");
		map.put("43", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/0AD74F3C1F309F169C33DC5901307461.mp4?t=1601281230&key=7C6F87440CA4E64FFA1339B83624F98A");
		map.put("44", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/0DF99F27A4DFE9839C33DC5901307461.mp4?t=1601281231&key=369BACE39A9955DE20F36DC65893688F");
		map.put("45", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/0F873DC16E7AD6EE9C33DC5901307461.mp4?t=1601281231&key=21AB1E760F5D9F9260DB77A16151FB73");
		map.put("46", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/530E2056DE5C76CF9C33DC5901307461.mp4?t=1601281231&key=F66CE9125F8D7D393EA34898464AE005");
		map.put("47", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/75E67D288C5DB2F19C33DC5901307461.mp4?t=1601281231&key=1C009542B2B910BA0D799E894C41F192");
		map.put("48", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/C968D4DC441B8FD49C33DC5901307461.mp4?t=1601281231&key=40286B2B5DA0D787FC7F23D5C63F453A");
		map.put("49", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/31B878925B4AB3239C33DC5901307461.mp4?t=1601281231&key=411B4179F2D6E611A6DBA45947C4468A");
		map.put("50", "http://18.cu.backup.bokecc.com/flvs/BDCCCDBC3050AF66/2017-12-27/F94712F642F906859C33DC5901307461.mp4?t=1601281231&key=D4923F762D52F034D4082CA3300CC0FE");
		return map;
	}
}
