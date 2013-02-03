package communication;

public interface Message {
    public int getReturnCode();
    public Content getContent();
    public String format();
}
