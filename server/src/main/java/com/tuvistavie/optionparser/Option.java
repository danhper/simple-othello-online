package com.tuvistavie.optionparser;

public class Option {
    protected char shortName;
    protected String longName;
    protected String description;
    
    public Option() {
    }
    
    public Option(char shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }
    
    public Option(char shortName, String longName, String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    public char getShortName() {
        return shortName;
    }

    public void setShortName(char shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
