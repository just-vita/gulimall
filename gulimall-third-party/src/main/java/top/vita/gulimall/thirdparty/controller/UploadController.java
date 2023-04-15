package top.vita.gulimall.thirdparty.controller;

import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.vita.common.utils.R;
import top.vita.gulimall.thirdparty.constant.ConnectTencentConstants;
import top.vita.gulimall.thirdparty.utils.ConnectTencentCloud;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class UploadController {

    @Autowired
    private ConnectTencentCloud tencentCloud;

    @PostMapping("/thirdparty/oss")
    public R upload(@RequestBody MultipartFile file){
        File tempFile = multipartFileToFile(file);
        String prefix = new SimpleDateFormat("yy-MM-dd").format(new Date());
        String key = "/gulimall/" + prefix + "/" + System.currentTimeMillis() + ".jpg";
        tencentCloud.uploadObject(tempFile, key);
        return R.ok().put("path", ConnectTencentConstants.TENCENT_PATH + key);
    }

    /**
     * 将MultipartFile转换为File
     * @parameter 要转换的文件
     * @return File
     */
    public static File multipartFileToFile(MultipartFile file) {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        fileName = fileName + UUID.randomUUID();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        try {
            File tempFile = File.createTempFile(fileName, suffix);
            file.transferTo(tempFile);
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
