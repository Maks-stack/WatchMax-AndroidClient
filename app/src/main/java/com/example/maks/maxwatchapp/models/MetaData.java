package com.example.maks.maxwatchapp.models;

/**
 * Created by Maks on 26/06/17.
 */

public class MetaData {
    private String id = "";
    private Boolean forceUpdate;
    private String downloadLink = "";
    private Integer versionNumber;

    public MetaData(String id, Boolean forceUpdate, String downloadLink, Integer versionNumber) {
        this.id = id;
        this.forceUpdate = forceUpdate;
        this.downloadLink = downloadLink;
        this.versionNumber = versionNumber;
    }

    public String getId() {

        return id;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }
}
