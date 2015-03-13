package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class TagsBean {

    // private variables
    private String id; // PK
    private String tagCategory;
    private String tagName;
    private int tagOrderInCategory;
    private String tagColor;
    private String tagAbstract;

    // Empty constructor
    public TagsBean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagCategory() {
        return tagCategory;
    }

    public void setTagCategory(String tagCategory) {
        this.tagCategory = tagCategory;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagOrderInCategory() {
        return tagOrderInCategory;
    }

    public void setTagOrderInCategory(int tagOrderInCategory) {
        this.tagOrderInCategory = tagOrderInCategory;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public String getTagAbstract() {
        return tagAbstract;
    }

    public void setTagAbstract(String tagAbstract) {
        this.tagAbstract = tagAbstract;
    }
}
