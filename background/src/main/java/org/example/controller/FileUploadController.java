package org.example.controller;

import org.example.pojo.Result;
import org.example.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        System.out.println("图像上传接口");
        // 把文件内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        // 保证文件名唯一,防止文件覆盖
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println( UUID.randomUUID());
        // file.transferTo(new File("D:\\ideaProjects\\big-event\\file\\" + filename));
        String url = AliOssUtil.upload(filename, file.getInputStream());
        return Result.success(url);
    }
}
