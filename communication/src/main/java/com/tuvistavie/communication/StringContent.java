package com.tuvistavie.othello.communication;

public class StringContent extends Content {
    protected String content;
    
    public StringContent() {
        this.content = null;
    }
    
    public StringContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String format() {
        if(this.content == null) {
            return "";
        } else {
            return this.content;
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.content.isEmpty();
    }
    
}
