package com.smart.cloud.fire.activity.UploadNFCInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;





public class newTest {

	private static File file;
	private static final String TAG="MainActivity";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		file = new File("D:z.jpg");
		uploadFile(file);
	}
	

    public static void uploadFile(File imageFile) {
        try {
            String requestUrl = "http://127.0.0.1:8080/fireSystem/UploadFileAction";
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", "123");
            params.put("areaId", "38");
            params.put("time", "1236563256693");
            FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");
            FileUtil.post(requestUrl, params, formfile);
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail");
        }
    }

}
