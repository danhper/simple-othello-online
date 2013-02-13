package com.tuvistavie.othello.communication;

public abstract class AbstractMessage implements Message {
    
    protected int returnCode;
    protected Content content;
    
    public AbstractMessage(int returnCode) {
        this.returnCode = returnCode;
        this.content = null;
    }
    
    public AbstractMessage(int returnCode, Content content) {
        this.returnCode = returnCode;
        this.content = content;
    }
    
    
    public int getReturnCode() {
        return this.returnCode;
    }
    
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
