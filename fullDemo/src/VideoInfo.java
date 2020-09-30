import com.alibaba.fastjson.JSONObject;

public class VideoInfo {
	public String id;
	public String ccurl;
	public String videoid;
	public String hlsKey;
	public String key;
	public String hlsUrl;
	public String processid;
@Override
public String toString() {
	return JSONObject.toJSONString(this);
}
public static void main(String[] args) {
	VideoInfo v = new VideoInfo();
	v.id="1";
	System.out.println(v);
}
}
