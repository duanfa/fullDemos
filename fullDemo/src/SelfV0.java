import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

public class SelfV0 {
public static void main(String[] args) {
	Configuration cfg = new Configuration();
	String url = "http://s1.robotun.cn/6Gy03wV-HdWMndeFzywa_p5CxjM=/lo7EGzgVpJ6kLIURW9KoJEGh1fu7?e=1602291805";
	//...其他参数参考类注释
	String accessKey = "nYlubQo3Cffp4Opj23CZ_2VOcZQiJBEX3igCAUsE";
	String secretKey = "u1DuhOhRXdO50ebmX7tXNU8i6n5zWhw2fKOaDcqq";
	String bucket = "static";
	String key = "lo7EGzgVpJ6kLIURW9KoJEGh1fu7";
	com.qiniu.util.Auth auth = Auth.create(accessKey, secretKey);
	String purl = auth.privateDownloadUrl(url);
	System.out.println(purl);
	BucketManager bucketManager = new BucketManager(auth, cfg);
	try {
	    FileInfo fileInfo = bucketManager.stat(bucket, key);
	    System.out.println(fileInfo.fsize);
	    System.out.println(fileInfo.mimeType);
	    System.out.println(fileInfo.putTime);
	} catch (QiniuException ex) {
	    System.err.println(ex.response.toString());
	}
}
}
