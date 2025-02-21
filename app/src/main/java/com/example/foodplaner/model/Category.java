package com.example.foodplaner.model;

class Category {
    private String strCategory;
    private String strCategoryDescription;
    private String idCategory;
    private String strCategoryThumb;

    public String getStrCategory() { return strCategory; }

    public Category(String strCategory, String strCategoryDescription, String idCategory, String strCategoryThumb) {
        this.strCategory = strCategory;
        this.strCategoryDescription = strCategoryDescription;
        this.idCategory = idCategory;
        this.strCategoryThumb = strCategoryThumb;
    }

    public void setStrCategory(String value) { this.strCategory = value; }

    public String getStrCategoryDescription() { return strCategoryDescription; }
    public void setStrCategoryDescription(String value) { this.strCategoryDescription = value; }

    public String getidCategory() { return idCategory; }
    public void setidCategory(String value) { this.idCategory = value; }

    public String getStrCategoryThumb() { return strCategoryThumb; }
    public void setStrCategoryThumb(String value) { this.strCategoryThumb = value; }
}
