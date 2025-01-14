package com.tvpss.entity;

import javax.persistence.*;
import com.tvpss.enums.ApprovalStatus;
import com.tvpss.enums.RecordStatus;
import java.util.Date;

@Entity
@Table(name = "schoolversion")
public class TVPSSVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "school_info_id", nullable = false)
    private SchoolInfo schoolInfo;

    private Integer version;
    private Integer approvedVersion;
    private ApprovalStatus status;

    private Boolean ppdApproval;
    private Boolean stateApproval;

    private String agency1Name;
    private String agency2Name;
    private String agencyManager1Name;
    private String agencyManager2Name;

    private Boolean isNoPhone;

    @Enumerated(EnumType.STRING)
    private RecordStatus recordEquipment;

    @Enumerated(EnumType.STRING)
    private RecordStatus tvpssStudio;

    @Enumerated(EnumType.STRING)
    private RecordStatus recInSchool;

    @Enumerated(EnumType.STRING)
    private RecordStatus recInOutSchool;

    @Enumerated(EnumType.STRING)
    private RecordStatus greenScreen;

    @Enumerated(EnumType.STRING)
    private RecordStatus isFillSchoolName;

    /*@Enumerated(EnumType.STRING)
    private RecordStatus isTvpssLogo;*/

    @Enumerated(EnumType.STRING)
    private RecordStatus isUploadYoutube;

    @Enumerated(EnumType.STRING)
    private RecordStatus isCollabAgency;

    private String tvpssLogo;

    /*@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;*/

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    /*@PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }*/

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getApprovedVersion() {
        return approvedVersion;
    }

    public void setApprovedVersion(Integer approvedVersion) {
        this.approvedVersion = approvedVersion;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Boolean getPpdApproval() {
        return ppdApproval;
    }

    public void setPpdApproval(Boolean ppdApproval) {
        this.ppdApproval = ppdApproval;
    }

    public Boolean getStateApproval() {
        return stateApproval;
    }

    public void setStateApproval(Boolean stateApproval) {
        this.stateApproval = stateApproval;
    }

    public String getAgency1Name() {
        return agency1Name;
    }

    public void setAgency1Name(String agency1Name) {
        this.agency1Name = agency1Name;
    }

    public String getAgency2Name() {
        return agency2Name;
    }

    public void setAgency2Name(String agency2Name) {
        this.agency2Name = agency2Name;
    }

    public String getAgencyManager1Name() {
        return agencyManager1Name;
    }

    public void setAgencyManager1Name(String agencyManager1Name) {
        this.agencyManager1Name = agencyManager1Name;
    }

    public String getAgencyManager2Name() {
        return agencyManager2Name;
    }

    public void setAgencyManager2Name(String agencyManager2Name) {
        this.agencyManager2Name = agencyManager2Name;
    }

    public Boolean getIsNoPhone() {
        return isNoPhone;
    }

    public void setIsNoPhone(Boolean isNoPhone) {
        this.isNoPhone = isNoPhone;
    }

    public RecordStatus getRecordEquipment() {
        return recordEquipment;
    }

    public void setRecordEquipment(RecordStatus recordEquipment) {
        this.recordEquipment = recordEquipment;
    }

    public RecordStatus getTvpssStudio() {
        return tvpssStudio;
    }

    public void setTvpssStudio(RecordStatus tvpssStudio) {
        this.tvpssStudio = tvpssStudio;
    }

    public RecordStatus getRecInSchool() {
        return recInSchool;
    }

    public void setRecInSchool(RecordStatus recInSchool) {
        this.recInSchool = recInSchool;
    }

    public RecordStatus getRecInOutSchool() {
        return recInOutSchool;
    }

    public void setRecInOutSchool(RecordStatus recInOutSchool) {
        this.recInOutSchool = recInOutSchool;
    }

    public RecordStatus getGreenScreen() {
        return greenScreen;
    }

    public void setGreenScreen(RecordStatus greenScreen) {
        this.greenScreen = greenScreen;
    }

    public RecordStatus getIsFillSchoolName() {
        return isFillSchoolName;
    }

    public void setIsFillSchoolName(RecordStatus isFillSchoolName) {
        this.isFillSchoolName = isFillSchoolName;
    }

    /*public RecordStatus getIsTvpssLogo() {
        return isTvpssLogo;
    }

    public void setIsTvpssLogo(RecordStatus isTvpssLogo) {
        this.isTvpssLogo = isTvpssLogo;
    }*/

    public RecordStatus getIsUploadYoutube() {
        return isUploadYoutube;
    }

    public void setIsUploadYoutube(RecordStatus isUploadYoutube) {
        this.isUploadYoutube = isUploadYoutube;
    }

    public RecordStatus getIsCollabAgency() {
        return isCollabAgency;
    }

    public void setIsCollabAgency(RecordStatus isCollabAgency) {
        this.isCollabAgency = isCollabAgency;
    }

    public String getTvpssLogo() {
        return tvpssLogo;
    }

    public void setTvpssLogo(String tvpssLogo) {
        this.tvpssLogo = tvpssLogo;
    }

    /*public Date getCreatedAt() {
        return createdAt;
    }*/

    /*public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }*/

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
