package communication;

public class UnicastMessageWrapper extends AbstractMessageWrapper {
    
    protected RemoteHost recipient;
    
    public UnicastMessageWrapper(Message message) {
        this.message = message;
        this.recipient = null;
    }
    
    public UnicastMessageWrapper(Message message, RemoteHost recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    public RemoteHost getRecipient() {
        return this.recipient;
    }

    public void setRecipient(RemoteHost recipient) {
        this.recipient = recipient;
    }
}
