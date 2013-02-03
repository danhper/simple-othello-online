package optionparser;

public class OptionWithArgument extends Option {
    protected String argument;
    
    public OptionWithArgument() {
    }
    
    public OptionWithArgument(char shortName, String longName, String argument) {
        super(shortName, longName);
        this.argument = argument;
    }
    
    public OptionWithArgument(char shortName, String longName, String argument, String description) {
        super(shortName, longName, description);
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
