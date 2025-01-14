package com.tvpss.service;

import com.tvpss.dao.SchoolInfoDao;
import com.tvpss.entity.SchoolInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class SchoolService {

    private final SchoolInfoDao schoolDao;
    //private final FileService fileService;

    @Autowired
    public SchoolService(SchoolInfoDao schoolDao/*, FileService fileService*/) {
        this.schoolDao = schoolDao;
        //this.fileService = fileService;
    }

    public SchoolInfo findSchoolInfo() {
        return schoolDao.findFirst();
    }
    
    public SchoolInfo findById(Long id) {
        SchoolInfo schoolInfo = schoolDao.findById(id);
        System.out.println("DEBUG: Retrieved SchoolInfo: " + schoolInfo);
        return schoolInfo;
    }


    public void saveOrUpdateSchoolInfo(SchoolInfo schoolInfo /*, MultipartFile schoolLogoFile*/) throws IOException {
        // if (schoolLogoFile != null && !schoolLogoFile.isEmpty()) {
        //     String savedPath = fileService.saveFile(schoolLogoFile, "images");
        //     schoolInfo.setSchoolLogo(savedPath); 
        // }

        if (schoolInfo.getId() == null) {
            schoolDao.save(schoolInfo);
        } else {
            updateExistingSchoolInfo(schoolDao.findById(schoolInfo.getId()), schoolInfo);
            schoolDao.save(schoolInfo);
        }
    }

    private void updateExistingSchoolInfo(SchoolInfo existing, SchoolInfo updated) {
        existing.setSchoolCode(updated.getSchoolCode());
        existing.setSchoolName(updated.getSchoolName());
        existing.setSchoolEmail(updated.getSchoolEmail());
        existing.setSchoolAddress1(updated.getSchoolAddress1());
        existing.setSchoolAddress2(updated.getSchoolAddress2());
        existing.setPostcode(updated.getPostcode());
        existing.setState(updated.getState());
        existing.setDistrict(updated.getDistrict());
        existing.setNoPhone(updated.getNoPhone());
        existing.setNoFax(updated.getNoFax());
        existing.setLinkYoutube(updated.getLinkYoutube());
    }
}
