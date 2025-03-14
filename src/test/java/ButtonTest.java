import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static org.junit.jupiter.api.Assertions.*;

import org.example.MainFrame;

public class ButtonTest {

    private JButton button;

    public void init() {
        MainFrame mainframe = new MainFrame();
        button = mainframe.createStyledButton("Click Me");
    }

    @Test
    public void testButtonText() {
        init();
        assertEquals("Click Me", button.getText(), "Button text should be 'Click Me'");
    }

    @Test
    public void testButtonHoverEffect() {
        init();
        // Simulate mouse entered event
        MouseListener[] listeners = button.getMouseListeners();
        MouseEvent event = new MouseEvent(button, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 0, false);
        for (MouseListener listener : listeners) {
            listener.mouseEntered(event);
        }
        assertEquals(new Color(60, 60, 60), button.getBackground(), "Button background should darken on hover");
    }
}
