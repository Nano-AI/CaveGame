import display.Window;
import game.scenes.Cave;

public class Main {
    public static void main(String[] args) {
        Window win = new Window();
        win.init("Dungeon Crawler", 1280, 720);
        win.scene = new Cave();
        win.loop();
    }
}