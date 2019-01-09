package com.mypinyougou.shop.controller;

import com.mypinyougou.entity.Result;
import com.mypinyougou.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/file")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;// 文件服务器地址

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        //1.获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        try {
            //2.获取FastDFSClient工具类客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //3.执行文件上传
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //4.拼接url
            String url = FILE_SERVER_URL + path;

            return new Result(true, url);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }

    }

}
