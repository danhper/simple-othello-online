package game;

public class Main {
  public static void main(String[] args) {
    BoardModel model = new BoardModel();
    BoardController controller = new BoardController(model, true);
    controller.launch();
  }
}
