package com.tvpss.model;

import com.tvpss.enums.EqStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

public class Equipment {
    private ObjectId id;
    private String name;
    private String type;
    private String location;
    private Date acquiredDate;
    private EqStatus status;

    public Equipment() {
    }

    public Equipment(String name, String type, String location, Date acquiredDate, StatusEnum status) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.acquiredDate = acquiredDate;
        this.status = status;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Date acquiredDate) {
        this.acquiredDate = acquiredDate; 
    }

    public EqStatus getStatus() {
        return status;
    }

    public void setStatus(EqStatus status) {
        this.status = status;
    }

    public String getStatusName() {
        return this.status != null ? this.status.name().replace("_", " ").toUpperCase() : "Unknown Status";
    }
}
