package com.tensquare.user.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.stereotype.Component;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.5.0</version>
</dependency>
*/
@Component
public class SendUtil {
    public CommonResponse SendUtil (String phone,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GBnCetGzEcwNFJXG3ht", "fPL5lnmRy9aW04eEXFYOHMhEpCkhgm");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "风林火山");
        request.putQueryParameter("TemplateCode", "SMS_192572363");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse commonResponse  = client.getCommonResponse(request);
            System.out.println(commonResponse.getData());
 	return commonResponse;
        } catch (ClientException e) {//commonResponse
            e.printStackTrace();
	 return null;
        }
    }
}
