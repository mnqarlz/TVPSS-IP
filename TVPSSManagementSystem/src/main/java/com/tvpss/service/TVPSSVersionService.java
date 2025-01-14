package com.tvpss.service;

import com.tvpss.dao.SchoolInfoDao;
import com.tvpss.dao.TVPSSVersionDao;
import com.tvpss.entity.SchoolInfo;
import com.tvpss.entity.TVPSSVersion;
import com.tvpss.entity.User;
import com.tvpss.enums.ApprovalStatus;
import com.tvpss.enums.RecordStatus;
import com.tvpss.enums.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TVPSSVersionService {

    private final TVPSSVersionDao tvpssVersionDao;
    private final SchoolInfoDao schoolDao;
    private final UserService userService;

    public TVPSSVersionService(TVPSSVersionDao tvpssVersionDao, SchoolInfoDao schoolDao, UserService userService) {
        this.tvpssVersionDao = tvpssVersionDao;
        this.schoolDao = schoolDao;
        this.userService = userService;
    }
    
    public void saveOrUpdateTVPSSVersion(TVPSSVersion tvpssVersion) {
        TVPSSVersion existingVersion = tvpssVersionDao.findBySchoolInfoId(tvpssVersion.getSchoolInfo().getId());

        if (existingVersion != null) {
            mergeVersions(existingVersion, tvpssVersion);
            tvpssVersionDao.save(existingVersion);
        } else {
            tvpssVersion.setStatus(ApprovalStatus.PENDING);
            tvpssVersion.setPpdApproval(false);
            tvpssVersion.setStateApproval(false);

            // Set RecordStatus values correctly
            tvpssVersion.setIsFillSchoolName(
            	tvpssVersion.getSchoolInfo().getSchoolName() != null ? RecordStatus.ADA : RecordStatus.TIADA
            );
            tvpssVersion.setIsNoPhone(
            	tvpssVersion.getSchoolInfo().getNoPhone() != null ? RecordStatus.ADA : RecordStatus.TIADA
            );
            tvpssVersion.setIsUploadYoutube(
                tvpssVersion.getSchoolInfo().getLinkYoutube() != null ? RecordStatus.ADA : RecordStatus.TIADA
            );
            tvpssVersion.setIsCollabAgency(
                (tvpssVersion.getAgency1Name() != null || tvpssVersion.getAgency2Name() != null)
                    ? RecordStatus.ADA
                    : RecordStatus.TIADA
            );

            // Determine and set the version
            tvpssVersion.setVersion(checkTVPSSVersion(tvpssVersion.getSchoolInfo(), tvpssVersion));

            tvpssVersionDao.save(tvpssVersion);
        }
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
    
    public List<TVPSSVersion> findAllByStatus(ApprovalStatus status) {
        return tvpssVersionDao.findAllByStatus(status);
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

    public void updateSchoolAndVersion(Long schoolInfoId, TVPSSVersion tvpssVersion) 
    {
    	// Fetch SchoolInfo by ID
        SchoolInfo schoolInfo = schoolDao.findById(schoolInfoId);
        if (schoolInfo == null) {
            throw new IllegalArgumentException("Invalid SchoolInfo ID. No such school exists.");
        }

        int version = checkTVPSSVersion(schoolInfo, tvpssVersion);
        tvpssVersion.setVersion(version);
        tvpssVersion.setSchoolInfo(schoolInfo);
        tvpssVersion.setStatus(ApprovalStatus.PENDING);
        tvpssVersion.setPpdApproval(false);
        tvpssVersion.setStateApproval(false);

        tvpssVersionDao.save(tvpssVersion);
    }


    private void mergeVersions(TVPSSVersion existingVersion, TVPSSVersion newVersion) {
        existingVersion.setAgency1Name(newVersion.getAgency1Name());
        existingVersion.setAgency2Name(newVersion.getAgency2Name());
        existingVersion.setAgencyManager1Name(newVersion.getAgencyManager1Name());
        existingVersion.setAgencyManager2Name(newVersion.getAgencyManager2Name());
        existingVersion.setRecordEquipment(newVersion.getRecordEquipment());
        existingVersion.setTvpssStudio(newVersion.getTvpssStudio());
        existingVersion.setRecInSchool(newVersion.getRecInSchool());
        existingVersion.setRecInOutSchool(newVersion.getRecInOutSchool());
        existingVersion.setGreenScreen(newVersion.getGreenScreen());

        // Set RecordStatus values
        existingVersion.setIsFillSchoolName(
            newVersion.getSchoolInfo().getSchoolName() != null ? RecordStatus.ADA : RecordStatus.TIADA
        );
        existingVersion.setIsNoPhone(
            newVersion.getSchoolInfo().getNoPhone() != null ? RecordStatus.ADA : RecordStatus.TIADA
        );
        existingVersion.setIsUploadYoutube(
            newVersion.getSchoolInfo().getLinkYoutube() != null ? RecordStatus.ADA : RecordStatus.TIADA
        );
        existingVersion.setIsCollabAgency(
            (newVersion.getAgency1Name() != null || newVersion.getAgency2Name() != null)
                ? RecordStatus.ADA
                : RecordStatus.TIADA
        );
    }


    public int checkTVPSSVersion(SchoolInfo schoolInfo, TVPSSVersion tvpssVersion) {
        String isFillSchoolName = schoolInfo.getSchoolName() != null ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String tvpssStudio = tvpssVersion.getTvpssStudio() != null ? tvpssVersion.getTvpssStudio().getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String recInSchool = tvpssVersion.getRecInSchool() != null ? tvpssVersion.getRecInSchool().getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String isUploadYoutube = schoolInfo.getLinkYoutube() != null ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String recInOutSchool = tvpssVersion.getRecInOutSchool() != null ? tvpssVersion.getRecInOutSchool().getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String isCollabAgency = (tvpssVersion.getAgency1Name() != null || tvpssVersion.getAgency2Name() != null) ? RecordStatus.ADA.getDisplayName() : RecordStatus.TIADA.getDisplayName();
        String greenScreen = tvpssVersion.getGreenScreen() != null ? tvpssVersion.getGreenScreen().getDisplayName() : RecordStatus.TIADA.getDisplayName();

        if (isFillSchoolName.equals(RecordStatus.ADA.getDisplayName()) &&
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
