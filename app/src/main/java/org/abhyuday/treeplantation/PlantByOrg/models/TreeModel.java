package org.abhyuday.treeplantation.PlantByOrg.models;

/**
 * A tree model that consists of id,name , genus, source , seedsapling, and sample imageurl
 */
public class TreeModel {
    int id;
    String name,genus, source, Seedsapling, imageUrl ;

    public TreeModel( String genus, int id, String imageUrl, String name, String seedsapling, String source) {
        this.genus = genus;
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.Seedsapling = seedsapling;
        this.source = source;
    }

    public TreeModel(){

    }

    //setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSeedsapling() {
        return Seedsapling;
    }

    public void setSeedsapling(String seedsapling) {
        Seedsapling = seedsapling;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
