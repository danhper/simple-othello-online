package communication;

public abstract class AbstractMessageWrapper implements MessageWrapper {
    protected Message message;
    
    @Override
    public Message getMessage() {
        return this.message;
    }
    
    public void setMessage(Message message) {
        this.message = message;
    }
}
