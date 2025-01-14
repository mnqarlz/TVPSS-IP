package com.tvpss.service;

import com.tvpss.dao.TVPSSVersionDao;
import com.tvpss.entity.SchoolInfo;
import com.tvpss.entity.TVPSSVersion;
import com.tvpss.enums.ApprovalStatus;
import com.tvpss.enums.RecordStatus;
import com.tvpss.enums.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class TVPSSVersionService {

    private final TVPSSVersionDao tvpssVersionDao;
    //private final FileService fileService;

    public TVPSSVersionService(TVPSSVersionDao tvpssVersionDao/*, FileService fileService*/) {
        this.tvpssVersionDao = tvpssVersionDao;
        //this.fileService = fileService;
    }

    public TVPSSVersion findById(Long id) {
        return tvpssVersionDao.findById(id);
    }

    public TVPSSVersion findBySchoolInfoId(Long schoolInfoId) {
        return tvpssVersionDao.findBySchoolInfoId(schoolInfoId);
    }

    public List<TVPSSVersion> findAll() {
        return tvpssVersionDao.findAll();
    }

    public void save(TVPSSVersion tvpssVersion) {
        tvpssVersionDao.save(tvpssVersion);
    }

    public void deleteById(Long id) {
        tvpssVersionDao.deleteById(id);
    }

    public void updateApprovalStatus(Long id, boolean isApproved, Role role) {
        TVPSSVersion version = findById(id);
        if (version == null) {
            throw new IllegalArgumentException("No TVPSSVersion found with ID: " + id);
        }

        if (role == Role.PPD_ADMIN) {
            if (isApproved) {
                version.setPpdApproval(true);
                version.setStatus(ApprovalStatus.PENDING); 
            } else {
                version.setPpdApproval(false);
                version.setStatus(ApprovalStatus.REJECTED); 
            }
        } else if (role == Role.STATE_ADMIN) {
            if (isApproved) {
                version.setStateApproval(true);
                version.setStatus(ApprovalStatus.APPROVED);
            } else {
                version.setStateApproval(false);
                version.setStatus(ApprovalStatus.REJECTED); 
            } 
        }

        version.setUpdatedAt(new java.util.Date());
        save(version);
    }

    public void updateSchoolAndVersion(SchoolInfo schoolInfo, TVPSSVersion tvpssVersion) {
        if (schoolInfo == null || schoolInfo.getId() == null) {
            throw new IllegalArgumentException("SchoolInfo must not be null and must have a valid ID.");
        }

        // Set School Officer
        //schoolInfo.setSchoolOfficer(schoolInfo.getUser().getName());

        // Commented out logic for TVPSS Logo
        // tvpssVersion.setIsTvpssLogo(tvpssVersion.getTvpssLogo() != null ? RecordStatus.ADA : RecordStatus.TIADA);

        // Determine the TVPSS version
        int version = checkTVPSSVersion(schoolInfo, tvpssVersion);
        tvpssVersion.setVersion(version);

        tvpssVersion.setStatus(ApprovalStatus.PENDING);
        tvpssVersion.setPpdApproval(tvpssVersion.getPpdApproval() != null ? tvpssVersion.getPpdApproval() : false);
        tvpssVersion.setStateApproval(tvpssVersion.getStateApproval() != null ? tvpssVersion.getStateApproval() : false);

        tvpssVersion.setSchoolInfo(schoolInfo);

        save(tvpssVersion);
    }

    public int checkTVPSSVersion(SchoolInfo schoolInfo, TVPSSVersion tvpssVersion) {
        String isFillSchoolName = schoolInfo.getSchoolName() != null ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        // Commented out TVPSS Logo validation
        // String isTvpssLogo = tvpssVersion.getTvpssLogo() != null ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String tvpssStudio = tvpssVersion.getTvpssStudio() != null ? tvpssVersion.getTvpssStudio().getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String recInSchool = tvpssVersion.getRecInSchool() != null ? tvpssVersion.getRecInSchool().getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String isUploadYoutube = schoolInfo.getLinkYoutube() != null ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String recInOutSchool = tvpssVersion.getRecInOutSchool() != null ? tvpssVersion.getRecInOutSchool().getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String isCollabAgency = (tvpssVersion.getAgency1Name() != null || tvpssVersion.getAgency2Name() != null) ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String greenScreen = tvpssVersion.getGreenScreen() != null ? tvpssVersion.getGreenScreen().getDisplayName() : RecordStatus.TIADA.getDisplayName();

        if (isFillSchoolName.equals(RecordStatus.ADA.getDisplayName()) &&
            // isTvpssLogo.equals(RecordStatus.ADA.getDisplayName()) && // Commented out
            tvpssStudio.equals(RecordStatus.ADA.getDisplayName()) &&
            recInSchool.equals(RecordStatus.ADA.getDisplayName()) &&
            isUploadYoutube.equals(RecordStatus.ADA.getDisplayName()) &&
            recInOutSchool.equals(RecordStatus.ADA.getDisplayName()) &&
            isCollabAgency.equals(RecordStatus.ADA.getDisplayName()) &&
            greenScreen.equals(RecordStatus.ADA.getDisplayName())) {
            return 4; 
        }

        if (isFillSchoolName.equals(RecordStatus.ADA.getDisplayName()) &&
            tvpssStudio.equals(RecordStatus.ADA.getDisplayName()) &&
            recInSchool.equals(RecordStatus.ADA.getDisplayName()) &&
            isUploadYoutube.equals(RecordStatus.ADA.getDisplayName())) {
            return 2;
        }

        if (isFillSchoolName.equals(RecordStatus.ADA.getDisplayName()) &&
            tvpssStudio.equals(RecordStatus.ADA.getDisplayName())) {
            return 1;
        }

        return 0; // Not satisfied
    }

    // Commented out saveTVPSSLogo method
    // public void saveTVPSSLogo(TVPSSVersion tvpssVersion, MultipartFile logoFile) throws IOException {
    //     String savedPath = fileService.saveFile(logoFile, "storage/tvpssLogos");
    //     tvpssVersion.setTvpssLogo(savedPath);
    //     save(tvpssVersion);
    // }

    public TVPSSVersion findOrCreateBySchoolInfo(SchoolInfo schoolInfo) {
        TVPSSVersion tvpssVersion = tvpssVersionDao.findBySchoolInfoId(schoolInfo.getId());
        if (tvpssVersion == null) {
            tvpssVersion = new TVPSSVersion();
            tvpssVersion.setSchoolInfo(schoolInfo);
            tvpssVersion.setStatus(ApprovalStatus.PENDING);
        }
        return tvpssVersion;
    }
}
