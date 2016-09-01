package org.abhyuday.treeplantation.PlantByOrg.models;

/**
 * A plantedTreeModel that consists of id,treeId, userId, name, genus, source, seed/sapling, imageUrl,treename, and
 * coordinates.
 */
public class PlantedTreeModel {

    int PT_id,PT_TreeId,PT_UserId;
    String PT_Name,T_genus, T_source, T_Seedsapling, T_imageUrl,T_Name ;
    double PT_Latitude,PT_Longitude;


    public PlantedTreeModel(int PT_id, double PT_Latitude, String t_name, double PT_Longitude, String PT_Name, int PT_TreeId, int PT_UserId, String t_genus, String t_imageUrl, String t_Seedsapling, String t_source) {
        this.PT_id = PT_id;
        this.PT_Latitude = PT_Latitude;
        this.PT_Longitude = PT_Longitude;
        this.PT_Name = PT_Name;
        this.PT_TreeId = PT_TreeId;
        this.PT_UserId = PT_UserId;
        T_genus = t_genus;
        T_imageUrl = t_imageUrl;
        T_Seedsapling = t_Seedsapling;
        T_source = t_source;
        T_Name = t_name;
    }

    public int getPT_id() {
        return PT_id;
    }

    public void setPT_id(int PT_id) {
        this.PT_id = PT_id;
    }

    public double getPT_Latitude() {
        return PT_Latitude;
    }

    public void setPT_Latitude(double PT_Latitude) {
        this.PT_Latitude = PT_Latitude;
    }

    public double getPT_Longitude() {
        return PT_Longitude;
    }

    public void setPT_Longitude(double PT_Longitude) {
        this.PT_Longitude = PT_Longitude;
    }

    public String getPT_Name() {
        return PT_Name;
    }

    public void setPT_Name(String PT_Name) {
        this.PT_Name = PT_Name;
    }

    public int getPT_TreeId() {
        return PT_TreeId;
    }

    public void setPT_TreeId(int PT_TreeId) {
        this.PT_TreeId = PT_TreeId;
    }

    public int getPT_UserId() {
        return PT_UserId;
    }

    public void setPT_UserId(int PT_UserId) {
        this.PT_UserId = PT_UserId;
    }

    public String getT_genus() {
        return T_genus;
    }

    public void setT_genus(String t_genus) {
        T_genus = t_genus;
    }

    public String getT_imageUrl() {
        return T_imageUrl;
    }

    public void setT_imageUrl(String t_imageUrl) {
        T_imageUrl = t_imageUrl;
    }

    public String getT_Seedsapling() {
        return T_Seedsapling;
    }

    public void setT_Seedsapling(String t_Seedsapling) {
        T_Seedsapling = t_Seedsapling;
    }

    public String getT_source() {
        return T_source;
    }

    public void setT_source(String t_source) {
        T_source = t_source;
    }


    public String getT_Name() {
        return T_Name;
    }

    public void setT_Name(String t_Name) {
        T_Name = t_Name;
    }
}
