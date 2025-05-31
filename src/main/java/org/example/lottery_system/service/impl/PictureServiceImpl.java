package org.example.lottery_system.service.impl;


import org.example.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.example.lottery_system.common.exception.ServiceException;
import org.example.lottery_system.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class PictureServiceImpl implements PictureService {
    @Value("${pic.local-path}")
    private String localPath;

    @Override
    public String savePicture(MultipartFile multipartFile) {

        // 创建目录
        File dir = new File(localPath);
        if (!dir.exists()) {
            dir.mkdirs();// 可以连续创建文件  mkdir不可以连续创建
        }

        // 创建索引
        // aaa.jpg -> xxx.jpg
        // aaa.jpg  .jpg  xxx  xxx.jpg
        String filename = multipartFile.getOriginalFilename();
        assert filename != null;
        String suffix = filename.substring(
                filename.lastIndexOf("."));
        filename = UUID.randomUUID() + suffix;

        // 图片保存
        try {
            multipartFile.transferTo(new File(localPath + "/" + filename));
        } catch (IOException e) {
            throw new ServiceException(ServiceErrorCodeConstants.PIC_UPLOAD_ERROR);
        }

        return filename;
    }
}
