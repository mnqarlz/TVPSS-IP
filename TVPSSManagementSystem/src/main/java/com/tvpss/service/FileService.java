package com.tvpss.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public String saveFile(MultipartFile file, String folder) throws IOException {
        String storagePath = "src/main/webapp/WEB-INF/storage/" + folder;

        File storageDir = new File(storagePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String filePath = storagePath + "/" + file.getOriginalFilename();
        File destinationFile = new File(filePath);

        file.transferTo(destinationFile);

        return "/storage/" + folder + "/" + file.getOriginalFilename();
    }
}
