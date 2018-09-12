package com.vida.ai.third.baidu.temp;

import com.vida.ai.third.baidu.VThirdBaiduService;

import java.net.URLEncoder;

public class BodyAnalysisSample {


	public static void main(String[] args) throws Exception {

		//返回字符串
		String imagePath = "F:\\videos\\ClothingWhite\\temp\\LM0A0215\\img_00004.jpg";
		String result = getBodyAnalysisResult(imagePath);
		System.out.println(result);
		//返回java对象
		//BodyAnalysisBean bodyAnalysisBean = getBodyAnalysisBean("本地图片路径", "自己的accesstoken");
		//System.out.println("图中有"+bodyAnalysisBean.getPerson_num()+"个人");
	}

	/**
	 * 人体关键点识别Demo
	 * @param imagePath
	 * @return 字符串
	 * @throws Exception
	 */
	public static String getBodyAnalysisResult(String imagePath) throws Exception{
		String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_analysis";
		VThirdBaiduService service = new VThirdBaiduService();
		service.getRealUrl(url);

		byte[] imgData = FileUtil.readFileByBytes(imagePath);
        String imgStr = Base64Util.encode(imgData);
		String param = "image=" + URLEncoder.encode(imgStr,"UTF-8");
        // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
		String result = HttpUtil.post(url, VThirdBaiduService.accessToken.getAccess_token(), param);
        //System.out.println(result);
        return result;
	}
}