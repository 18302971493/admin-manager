package com.ytain.util;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;

/**
 * @author 胡国栋
 * @version 1.0.0
 * @description
 * @date 2019-03-27 17:13
 */
public class FileUtils {
    public static String getFileName(){
        long now = Instant.now().atOffset(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        return String.valueOf(now);
    }
    public static String getServerDoMain(HttpServletRequest request){
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        return basePath;
    }

    public static String upload(MultipartFile file, String subName, HttpServletRequest request){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime())+file.getOriginalFilename();
        String subPath=subName+"/"+calendar.get(Calendar.YEAR)+"/"+Integer.valueOf(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/";
        File path= null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
            File upload=new File(path.getAbsolutePath(),"/"+subPath);
            if(!upload.getParentFile().exists()){
                upload.mkdirs();
            }
            file.transferTo(new File(upload.getAbsolutePath()+"/"+dateName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getServerDoMain(request)+"/"+subPath+"/"+dateName;
    }
}
