package com.ytain.util;


import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


/**
 * @author 小丶果冻
 * @title: HttpUtil
 * @projectName admin
 * @description: TODO
 * @date 2018年10月26日 星期五 15:57
 */
public class HttpUtil {


    private static Logger logger= LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送post请求
     * @param reqURL
     * @param decodeCharset
     * @return
     */
    public static String postData(String reqURL, String decodeCharset) {
        // 响应内容
        String responseContent = null;
        // 创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建org.apache.http.client.methods.HttpGet
        HttpGet httpGet = new HttpGet(reqURL);
        try {
            // 执行GET请求   iso8859-1
            CloseableHttpResponse response = httpClient.execute(httpGet);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                // Consume response content
                EntityUtils.consume(entity);
            }
            response.close(); // 关闭连接,释放资源
        } catch (ClientProtocolException e) {
            logger.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
            }
        }
        return responseContent;
    }

    public static void main(String[] args) {
        System.out.println(HttpUtil.postData("http://www.baidu.com",""));
    }
}
