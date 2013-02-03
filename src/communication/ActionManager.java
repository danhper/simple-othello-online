package communication;

public final class ActionManager {
    
    public static final String[] actions = createActions();
    public static final String[] actionsRegex = createRegex();
    
    private ActionManager() {
        
    }
    
    private static String[] createActions() {
        return new String[] {
          "play", "color", "connect", "waiting", "ready", "over", "error", "quit"  
        };
    }
    
    private static String[] createRegex() {
        return new String[] {
          "([0-1])-([0-8]),([0-8])", "([0-1])", null, null, null, "([0-2])", ".*", null 
        };
    }
    
    public static boolean isValidAction(String action) {
        for(String s : ActionManager.actions) {
            if(action.equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isValidActionNumber(int id) {
        return id >= 0 && id < ActionManager.actions.length;
    }
    
    public static String getActionText(String action) {
        if(action.equals("action")) {
            return "";
        } else if(action.equals("connect")) {
            return "A player has joined the game.";
        } else if(action.equals("quit")) {
            return "A Player has left the game."; 
        } else {
            return "Unknown action.";
        }
    }
    
    public static int getActionNumber(String action) {
        for(int i = 0; i < ActionManager.actions.length; i++) {
            if(action.equals(ActionManager.actions[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public static String getAction(int id) {
        if(id >= ActionManager.actions.length) {
            return null;
        }
        return ActionManager.actions[id];
    }

}
