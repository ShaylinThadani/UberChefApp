package com.example.onlyfoods_seg;

public class Complaint {
    private String _id;
    private String _chefName;
    private String _description;
    private String _complaintId;

    public Complaint(String id, String chefName, String description, String complaintId) {
        _id = id;
        _chefName = chefName;
        _description = description;
        _complaintId = complaintId;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public void setChefName(String chefName) {
        _chefName = chefName;
    }

    public String getChefName() {
        return _chefName;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public String getComplaintId(){return _complaintId;}
}
