package com.ytain.modules.common;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.vo.Result;
import com.ytain.modules.sys.entity.Setting;
import com.ytain.util.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

/**
 * @author: 六娃
 * @description: 上传文件
 * @create: 2018-12-27 11:32
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/common/upload")
public class UploadController {
    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/file",method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) String base64,
                                 HttpServletRequest request) {
        // IP限流 在线Demo所需 5分钟限1个请求
        if(StrUtil.isNotBlank(base64)){
            // base64上传
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }
        String result = "";
        String fKey = CommonUtil.renamePic(file.getOriginalFilename());
        try {
            fileUtil.localUpload(file, fKey);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResultUtil<Object>().setErrorMsg(e.toString());
        }
        JSONObject os = fileUtil.getOssSetting();
            result = os.getString("http") + os.getString("endpoint") + "/";
        return new ResultUtil<Object>().setData(result);
    }

}
