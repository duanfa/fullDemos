import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class SelfV1 {
public static void main(String[] args) {
	Configuration cfg = new Configuration();
	//...其他参数参考类注释
	UploadManager uploadManager = new UploadManager(cfg);
	//...生成上传凭证，然后准备上传
	String accessKey = "nYlubQo3Cffp4Opj23CZ_2VOcZQiJBEX3igCAUsE";
	String secretKey = "u1DuhOhRXdO50ebmX7tXNU8i6n5zWhw2fKOaDcqq";
	String bucket = "static";
	//如果是Windows情况下，格式是 D:\\qiniu\\test.png
	String localFilePath = "/home/duanfa/Downloads/F1605312C7D6466D9C33DC5901307461.mp4";
	//默认不指定key的情况下，以文件内容的hash值作为文件名
	String key = null;
	Auth auth = Auth.create(accessKey, secretKey);
	String upToken = auth.uploadToken(bucket);
	try {
	    Response response = uploadManager.put(localFilePath, key, upToken);
	    //解析上传成功的结果
	    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
	    System.out.println(putRet.key);
	    System.out.println(putRet.hash);
	} catch (QiniuException ex) {
	    Response r = ex.response;
	    System.err.println(r.toString());
	    try {
	        System.err.println(r.bodyString());
	    } catch (QiniuException ex2) {
	        //ignore
	    }
	}
}
}
