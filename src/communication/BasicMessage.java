package communication;


public class BasicMessage extends AbstractMessage {
    
    public BasicMessage(int returnCode) {
        super(returnCode);
    }
    
    public BasicMessage(int returnCode, Content content) {
        super(returnCode, content);
    }
    
    @Override
    public String format() {
        if(this.content == null || this.content.isEmpty()) {
            return ActionManager.getAction(this.returnCode);
        } else {
            return ActionManager.getAction(this.returnCode) + "/" + this.content.format();
        }
    }

}
