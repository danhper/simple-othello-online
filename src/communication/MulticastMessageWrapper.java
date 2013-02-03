package communication;

import java.util.ArrayList;


public class MulticastMessageWrapper extends AbstractMessageWrapper {
    
    protected ArrayList<RemoteHost> recipients;
    
    public MulticastMessageWrapper(Message message) {
        this.message = message;
        this.recipients = new ArrayList<RemoteHost>();
    }
    
    public MulticastMessageWrapper(Message message, ArrayList<RemoteHost> recipients) {
        this.message = message;
        this.recipients = recipients;
    }
    
    public MulticastMessageWrapper(UnicastMessageWrapper messageContainer) {
        this.message = messageContainer.getMessage();
        this.recipients = new ArrayList<RemoteHost>();
        this.recipients.add(messageContainer.getRecipient());
    }

    public ArrayList<RemoteHost> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(ArrayList<RemoteHost> recipients) {
        this.recipients = recipients;
    }
    
    public void addRecipient(RemoteHost recipient) {
        this.recipients.add(recipient);
    }
}
