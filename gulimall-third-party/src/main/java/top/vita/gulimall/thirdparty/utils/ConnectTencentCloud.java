package top.vita.gulimall.thirdparty.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.vita.gulimall.thirdparty.constant.ConnectTencentConstants;

import java.io.File;
import java.util.List;

/**
 * 连接腾讯云的存储桶
 */
@Component
@Slf4j
public class ConnectTencentCloud {
    protected static COSClient cosClient;
    protected String secretId = ConnectTencentConstants.SECRETID;
    protected String secretKey = ConnectTencentConstants.SECRETKEY;
    protected String bucketName = ConnectTencentConstants.BUCKETNAME;
    protected String apCity = ConnectTencentConstants.APCITY;

    public ConnectTencentCloud() {
        // 初始化用户身份信息
        BasicCOSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 设置存储桶的地区
        Region region = new Region(apCity);
        ClientConfig clientConfig = new ClientConfig(region);
        // 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
    }

    /**
     * 上传文件
     * @parameter fileUrl 上传文件地址
     * @parameter fileKey 文件对象名称
     * @parameter @return 对象列表
     */
    public PutObjectResult uploadObject(String fileUrl, String fileKey) {
        try {
            // 指定要上传的文件
            File localFile = new File(fileUrl);
            // fileKey 指定要上传到 COS 上对象键
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return putObjectResult;
        } catch (CosServiceException serverException) {
            log.error(serverException.getErrorMessage());
            throw new RuntimeException("上传文件平台Server异常" + serverException.getErrorMessage());
        } catch (CosClientException clientException) {
            log.error(clientException.getMessage());
            throw new RuntimeException("上传文件平台Client异常" + clientException.getMessage());
        }
    }

    /**
     * 上传文件
     * @parameter file 上传文件
     * @parameter fileKey 文件对象名称
     * @parameter @return 对象列表
     */
    public PutObjectResult uploadObject(File file, String fileKey) {
        try {
            // fileKey 指定要上传到 COS 上对象键
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return putObjectResult;
        } catch (CosServiceException serverException) {
            log.error(serverException.getErrorMessage());
            throw new RuntimeException("上传文件平台Server异常" + serverException.getErrorMessage());
        } catch (CosClientException clientException) {
            log.error(clientException.getMessage());
            throw new RuntimeException("上传文件平台Client异常" + clientException.getMessage());
        }
    }

    /**
     * 查询存储桶文件
     * @parameter prefix 设置查询目录
     * @parameter isAll 是否查询全部（包括子目录）
     */
    public List<COSObjectSummary> queryObject(String prefix, boolean isAll) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix(prefix);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        if (isAll){
            listObjectsRequest.setDelimiter(null);
        }else{
            listObjectsRequest.setDelimiter("/");
        }
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        List<COSObjectSummary> cosObjectSummaries;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return null;
            } catch (CosClientException e) {
                e.printStackTrace();
                return null;
            }
//            // common prefixs表示所有列出的文件夹
//            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                System.out.println(key);
            }

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        return cosObjectSummaries;
    }

    /**
     * 下载文件
     * @parameter key 指定对象在 COS 上的对象键
     * @parameter path 指定要下载到的本地路径
     */
    public void downLoadObject(String key, String path) {
        try {
            // 指定要下载到的本地路径
            File downFile = new File(path);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    /**
     * 删除对象
     * @parameter key 指定对象在 COS 上的对象键
     */
    public void deleteObject(String key) {
        try {
            // 指定对象在 COS 上的对象键
            cosClient.deleteObject(bucketName, key);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConnectTencentCloud cloud = new ConnectTencentCloud();
//        String fileUrl = "H:\\常用\\英语\\adv\\QQ截图20211209134133.png";
//        cloud.uploadObject(fileUrl, "/img/test.png");

//        List<COSObjectSummary> cosObjectSummaries = cloud.queryObject("img/个人博客/", false);
//        System.out.println(cosObjectSummaries.size());

//        String key = "test.png";
//        String path = "C:\\Users\\dell\\Desktop\\test.png";
//        cloud.downLoadObject(key, path);

//        cloud.deleteObject("test.png");

    }

}
