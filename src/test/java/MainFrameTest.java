import org.example.MainFrame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.reflect.Method;
import java.util.List;

//execute mvn test in CGHPM directory
public class MainFrameTest {

    private JButton button;
    MainFrame mainFrame = new MainFrame();
    Method method;

    //initialize Sign In button to test
    public void init() {
        try {
            //use reflection to get the private method
            method = MainFrame.class.getDeclaredMethod("createStyledButton", String.class, Color.class, Color.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        try {
            button = (JButton) method.invoke(mainFrame, "Sign In", new Color(45, 45, 45), Color.WHITE);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //test Sign In button text
    @Test
    public void testButtonText() {
        init();
        assertEquals("Sign In", button.getText(), "Button text should be 'Sign In'");
    }

    //test background colour change on mouse hover
    @Test
    public void testButtonHoverEffect() {
        init();
        //simulate mouse entered event
        MouseListener[] listeners = button.getMouseListeners();
        MouseEvent event = new MouseEvent(button, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 0, false);
        for (MouseListener listener : listeners) {
            listener.mouseEntered(event);
        }
        assertEquals(new Color(31, 31, 31), button.getBackground(), "Button background should darken on hover");
    }

    //test predictive search results
    @Test
    public void testAnimalDisplaySearchResults() throws Exception {
        //use reflection to get the private method
        method = MainFrame.class.getDeclaredMethod("displaySearchResults", String.class);
        method.setAccessible(true);  // Make the private method accessible

        List<String> results = (List<String>) method.invoke(mainFrame, "polar");
        assertTrue(results.contains("Animal: Polar Bear"), "The result list does not contain Polar Bear");
    }

    @Test
    public void testAnimalsDisplaySearchResults() throws Exception {
        //use reflection to get the private method
        method = MainFrame.class.getDeclaredMethod("displaySearchResults", String.class);
        method.setAccessible(true);  // Make the private method accessible

        List<String> results = (List<String>) method.invoke(mainFrame, "loon");
        assertTrue(results.contains("Animal: Common Loon"), "The result list does not contain Polar Bear");
    }

    @Test
    public void testProvinceDisplaySearchResults() throws Exception {
        //use reflection to get the private method
        method = MainFrame.class.getDeclaredMethod("displaySearchResults", String.class);
        method.setAccessible(true);  // Make the private method accessible

        List<String> results = (List<String>) method.invoke(mainFrame, "on");
        assertTrue(results.contains("Province: Ontario"), "The result list does not contain Ontario");
        assertTrue(results.contains("Province: Yukon"), "The result list does not contain Yukon");
    }

    @Test
    public void testLoadAnimalNames() throws Exception {
        //use reflection to get the private method
        method = MainFrame.class.getDeclaredMethod("loadAnimalNames");
        method.setAccessible(true);  // Make the private method accessible

        List<String> animalNames = (List<String>) method.invoke(mainFrame);
        assertEquals(65, animalNames.size());
    }

    @Test
    public void testAnimalsPanelContent() {

        //let jFrame load before testing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = mainFrame.showNunavutAnimals();
            JScrollPane animalsScrollPane = (JScrollPane) frame.getContentPane().getComponent(0);
            JPanel animalsPanel = (JPanel) animalsScrollPane.getViewport().getView();

            assertNotNull(animalsPanel, "animalsPanel is null");
            assertEquals(5, animalsPanel.getComponentCount(), "animalsPanel does not contain 5 components");

            JLabel firstLabel = (JLabel) animalsPanel.getComponent(1);  // The JLabel inside the first animal panel
            assertEquals("Polar Bear", firstLabel.getText(), "First label does not contain Polar Bear");
        });
    }

    @Test
    public void testArticlesFrame() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        method = MainFrame.class.getDeclaredMethod("openArticlesFrame");
        method.setAccessible(true);  // Make the private method accessible
        JFrame frame = (JFrame) method.invoke(mainFrame);

        SwingUtilities.invokeLater(() -> {
            JPanel articlesScrollPane = (JPanel) frame.getContentPane().getComponent(0);
            Component[] articlesPanel = articlesScrollPane.getComponents();

            assertNotNull(articlesPanel, "articlesPanel is null");
            assertEquals(5, articlesPanel.length, "articlesPanel does not contain 5 components");

            JLabel firstLabel = (JLabel) articlesPanel[1];  // The JLabel inside the first animal panel
            assertEquals("Climate Change & Wildlife", firstLabel.getText(), "First label does not contain 'Climate Change & Wildlife'");
        });
    }

}
