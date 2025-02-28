import org.example.AnimalInfo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JButton viewMapButton, loginButton, exitButton;
    private JPanel statusPanel, mapPanel;
    private JLabel mapLabel, titleLabel;
    private JLayeredPane layeredPane;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Canadian Wildlife");
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Title Label
        titleLabel = new JLabel("Wildlife Information Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(9, 60, 119));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(getWidth(), 60));

// Add this line for a 3D effect
        titleLabel.setUI(new javax.swing.plaf.basic.BasicLabelUI());

        add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(new Color(220, 220, 220));

        viewMapButton = createStyledButton("View Map");
        loginButton = createStyledButton("Login");
        exitButton = createStyledButton("Exit", Color.RED, Color.WHITE);

        exitButton.addActionListener(e -> System.exit(0));
        viewMapButton.addActionListener(evt -> loadMap());
        loginButton.addActionListener(evt -> loginActionPerformed());

        topPanel.add(viewMapButton);
        topPanel.add(loginButton);
        topPanel.add(exitButton);
        add(topPanel, BorderLayout.SOUTH);

        // User Status Panel
        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("User Status"));
        statusPanel.setPreferredSize(new Dimension(250, getHeight()));

        add(statusPanel, BorderLayout.WEST);

        // Map Panel
        mapPanel = new JPanel(new BorderLayout());
        mapLabel = new JLabel("", SwingConstants.CENTER);
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        mapLabel.setBounds(0, 0, 800, 600);
        layeredPane.add(mapLabel, Integer.valueOf(0));

        mapPanel.add(layeredPane, BorderLayout.CENTER);
        mapPanel.setBackground(Color.LIGHT_GRAY);
        add(mapPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(9, 60, 119), Color.WHITE);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void loginActionPerformed() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {"Username:", usernameField, "Password:", passwordField};

        while (true) {
            int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);

            if (option != JOptionPane.OK_OPTION) return; // Cancelled

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required.", "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break; // Success
            }
        }

        statusPanel.removeAll();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

        statusPanel.add(new JLabel("Welcome, " + usernameField.getText() + "!"));
        statusPanel.add(Box.createVerticalStrut(10));

        // Create buttons with updated colors
        JButton newsletterButton = createStyledButton("Subscribe to Newsletter", new Color(0, 153, 204), Color.WHITE);
        JButton articlesButton = createStyledButton("View Articles", new Color(255, 140, 0), Color.WHITE);
        JButton endangeredButton = createStyledButton("Endangered List", new Color(105, 215, 230), Color.WHITE);
        JButton huntableButton = createStyledButton("Not for Hunt", new Color(123, 207, 243), Color.WHITE);
        JButton factsButton = createStyledButton("Some Facts", new Color(243, 105, 105, 255), Color.WHITE);
        JButton emergencyButton = createStyledButton("Emergency Contact", new Color(255, 87, 51), Color.WHITE);
        JButton resourcesButton = createStyledButton("Resources", new Color(129, 129, 135), Color.WHITE);

        // Add action listeners
        newsletterButton.addActionListener(e -> subscribeToNewsletter());
        articlesButton.addActionListener(e -> openArticlesPage());
        endangeredButton.addActionListener(e -> showEndangeredAnimals());
        huntableButton.addActionListener(e -> showLegalHuntingList());
        factsButton.addActionListener(e -> showAnimalFacts());
        emergencyButton.addActionListener(e -> showEmergencyContacts());
        resourcesButton.addActionListener(e -> showResources());

        // Add buttons in the correct order
        statusPanel.add(newsletterButton);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(articlesButton);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(endangeredButton);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(huntableButton);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(factsButton);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(emergencyButton);

        // Add a flexible space to push "Resources" to the bottom
        statusPanel.add(Box.createVerticalGlue());
        statusPanel.add(resourcesButton);

        statusPanel.revalidate();
        statusPanel.repaint();
    }

    private void showEndangeredAnimals() {
        String[] endangeredAnimals = {"Whooping Crane", "Vancouver Island Marmot", "Burrowing Owl", "North Atlantic Right Whale"};
        JOptionPane.showMessageDialog(this, "⚠️ Top Threatened Species:\n" + String.join("\n", endangeredAnimals), "Top Threatened", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLegalHuntingList() {
        String[] legalAnimals = {"White-tailed Deer", "Moose", "Black Bear", "Canada Goose"};
        JOptionPane.showMessageDialog(this, "📜 Hunting Guide:\n" + String.join("\n", legalAnimals), "Hunting Guide", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEmergencyContacts() {
        JOptionPane.showMessageDialog(this, "🚨 Report Poaching:\n\n📞 Call: 1-800-ILLEGAL-WILDLIFE\n📧 Email: report@wildlifeprotection.ca", "Report Poaching", JOptionPane.WARNING_MESSAGE);
    }

    private void showAnimalFacts() {
        String[] facts = {
                "The beaver is Canada's national animal.",
                "Polar bears are considered marine mammals.",
                "Canada has over 200 species of mammals.",
                "The Canadian Lynx has huge paws for walking on snow."
        };
        JOptionPane.showMessageDialog(this, "🌎 Wild Facts:\n" + String.join("\n", facts), "Wild Facts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showResources(){

    }

    private void subscribeToNewsletter() {
        while (true) {
            String email = JOptionPane.showInputDialog(this, "Enter your Gmail to subscribe:", "Newsletter Subscription", JOptionPane.PLAIN_MESSAGE);

            if (email == null) return; // Cancelled

            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Invalid email! Must end with @gmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Subscription successful! Newsletters will be sent to: " + email);
                break;
            }
        }
    }

    private void openArticlesPage() {
        JFrame articlesFrame = new JFrame("Articles");
        articlesFrame.setSize(600, 400);
        articlesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        articlesFrame.setVisible(true);
    }

    private void loadMap() {
        ImageIcon mapIcon = new ImageIcon("src/main/resources/stuuu.png");
        Image scaledImage = mapIcon.getImage().getScaledInstance(mapPanel.getWidth(), mapPanel.getHeight(), Image.SCALE_SMOOTH);
        mapLabel.setIcon(new ImageIcon(scaledImage));

        int x = (mapPanel.getWidth() - mapLabel.getIcon().getIconWidth()) / 2;
        int y = (mapPanel.getHeight() - mapLabel.getIcon().getIconHeight()) / 2;
        mapLabel.setBounds(x, y, mapLabel.getIcon().getIconWidth(), mapLabel.getIcon().getIconHeight());

        // Add province buttons
        addProvinceButton("Ontario", 420, 460);
        addProvinceButton("Quebec", 550, 420);
        addProvinceButton("Nova Scotia", 680, 470);
        addProvinceButton("New Brunswick", 620, 480);
        addProvinceButton("Manitoba", 370, 370);
        addProvinceButton("Saskatchewan", 300, 380);
        addProvinceButton("Alberta", 250, 350);
        addProvinceButton("British Columbia", 180, 310);
        addProvinceButton("Yukon", 190, 165);
        addProvinceButton("Prince Edward Island", 650, 465);
        addProvinceButton("Newfoundland and Labrador", 630, 360);
        addProvinceButton("Northwest Territories", 250, 200);
        addProvinceButton("Nunavut", 350, 250);

        JLabel instructionLabel = new JLabel("<html><b>Click on the Green Flags to View Animals of That Province \uD83D\uDEA9.</b></html>");
        instructionLabel.setOpaque(true);
        instructionLabel.setBackground(new Color(223, 246, 255)); // Light cool blue
        instructionLabel.setForeground(new Color(0, 51, 102)); // Dark blue for readability
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2), // Dark blue border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));

        // Position in the top-right of the map
        int labelWidth = 260, labelHeight = 35;
        int labelX = mapLabel.getIcon().getIconWidth() - labelWidth - 15; // Right-aligned
        int labelY = 15; // 15px from the top

        instructionLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        layeredPane.add(instructionLabel, Integer.valueOf(2)); // Higher layer for visibility

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void addProvinceButton(String province, int x, int y) {
        ImageIcon provinceIcon = new ImageIcon("src/main/resources/flag.png");
        JButton provinceButton = new JButton(provinceIcon);
        provinceButton.setBounds(x, y, provinceIcon.getIconWidth(), provinceIcon.getIconHeight());
        provinceButton.setBorderPainted(false);
        provinceButton.setContentAreaFilled(false);
        provinceButton.setFocusPainted(false);

        provinceButton.addActionListener(e -> {
            if (province.equals("Ontario")) {
                showOntarioAnimals();
            }
            else if (province.equals("Nunavut")) {
                showNunavutAnimals();
            }
            else if (province.equals("Quebec")) {
                showQuebecAnimals();
            }
            else if (province.equals("Nova Scotia")) {
                showNovaScotiaAnimals();
            }
            else if (province.equals("Manitoba")) {
                showManitobaAnimals();
            }
            else if (province.equals("Saskatchewan")) {
                showSaskatchewanAnimals();
            }
            else if (province.equals("Alberta")) {
                showAlbertaAnimals();
            }
            else if (province.equals("British Columbia")) {
                showBritishColumbiaAnimals();
            }
            else if (province.equals("Yukon")) {
                showYukonAnimals();
            }
            else if (province.equals("Northwest Territories")) {
                showNorthwestTerritoriesAnimals();
            }
            else if (province.equals("New Brunswick")) {
                showNewBrunswickAnimals();
            }
            else if (province.equals("Newfoundland and Labrador")) {
                showNewfoundlandandLabradorAnimals();
            }
            else if (province.equals("Prince Edward Island")) {
                showPrinceEdwardIslandAnimals();
            }
        });

        layeredPane.add(provinceButton, Integer.valueOf(1));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void showOntarioAnimals() {
        JFrame ontarioFrame = new JFrame("Top 5 Native Animals in Ontario");
        ontarioFrame.setSize(800, 750);
        ontarioFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        ontarioFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Moose</b><br>The moose is the largest member of the deer family, known for its massive size, long legs, and distinctive broad, palmate antlers (in males). Moose are commonly found in forested areas near lakes and rivers.</html>",
                "<html><b>Beaver</b><br>The beaver is a large, semi-aquatic rodent known for building dams, lodges, and canals. It has strong teeth for gnawing on wood and is an ecosystem engineer, creating wetlands that benefit many species.</html>",
                "<html><b>Eastern Wolf</b><br>The Eastern wolf is a subspecies of the grey wolf, smaller than its western counterpart, and is native to Ontario's forests. It has a keen sense of smell and is an apex predator.</html>",
                "<html><b>Common Loon</b><br>Known for its haunting call, the common loon is a large water bird with striking black-and-white plumage and a sharp, pointed bill. It’s known for its excellent diving and swimming abilities.</html>",
                "<html><b>Blanding's Turtle</b><br>Recognizable by its yellow throat and smooth, domed shell, the Blanding’s turtle is a protected species in Ontario. It’s an aquatic turtle that prefers shallow wetlands and marshes.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Ontario/moose.jpg",
                "src/main/resources/Ontario/beaver.jpg",
                "src/main/resources/Ontario/wolf.png",
                "src/main/resources/Ontario/loon.jpg",
                "src/main/resources/Ontario/turtle.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        ontarioFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(246, 185, 163));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        ontarioFrame.add(animalsPanel, BorderLayout.CENTER);
        ontarioFrame.add(readMoreButton, BorderLayout.SOUTH);

        ontarioFrame.setVisible(true);
    }
    private void showQuebecAnimals() {
        JFrame quebecFrame = new JFrame("Top 5 Native Animals in Quebec");
        quebecFrame.setSize(800, 750);
        quebecFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        quebecFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);
        String[] animals = {
                "<html><b>Canada Lynx</b><br>A medium-sized wildcat with large paws adapted for walking on snow. It has tufted ears and a thick fur coat that helps it survive the cold Quebec winters.</html>",
                "<html><b>Atlantic Puffin</b><br>A small seabird with a colorful beak and strong swimming ability. Often called the \"clown of the sea,\" it is a well-known bird of the Gulf of St. Lawrence and the Quebec coast.</html>",
                "<html><b>Black Bear</b><br>One of Quebec's most common large mammals, the black bear is highly adaptable and can be found in various forested environments. It hibernates during the winter.</html>",
                "<html><b>Harlequin Duck</b><br>A strikingly colored duck with white markings on a dark body. It prefers fast-moving streams and coastal waters, making it unique among waterfowl.</html>",
                "<html><b>Striped Skunk</b><br>A small, black-and-white mammal known for its distinctive stripe pattern and its ability to spray a foul-smelling liquid as a defense mechanism. It is nocturnal and forages at night.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Quebec/lynx.jpg",
                "src/main/resources/Quebec/puffin.jpeg",
                "src/main/resources/Quebec/bear.jpg",
                "src/main/resources/Quebec/duck.jpg",
                "src/main/resources/Quebec/skunk.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);

        }
        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        quebecFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(163, 246, 224));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        quebecFrame.add(animalsPanel, BorderLayout.CENTER);
        quebecFrame.add(readMoreButton, BorderLayout.SOUTH);

        quebecFrame.setVisible(true);
    }
    private void showNovaScotiaAnimals() {
        JFrame novascotiaFrame = new JFrame("Top 5 Native Animals in Nova Scotia");
        novascotiaFrame.setSize(800, 750);
        novascotiaFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        novascotiaFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);
        String[] animals = {
                "<html><b>White-tailed Deer</b><br>A graceful and common deer species, known for its distinctive white tail that it raises when alarmed. They are highly adaptable and found throughout the province.</html>",
                "<html><b>Bald Eagle</b><br>A powerful bird of prey and the national symbol of the United States, the bald eagle is also commonly found in Nova Scotia. It has sharp talons and an impressive wingspan, making it a dominant predator in its environment.</html>",
                "<html><b>Eastern Coyote</b><br>Larger than western coyotes, the Eastern coyote is a top predator in Nova Scotia, often mistaken for a wolf due to its size. It is highly adaptable and thrives in various environments.</html>",
                "<html><b>Harbor Seal</b><br>A playful marine mammal commonly seen along Nova Scotia’s coast. Harbor seals are excellent swimmers and often rest on rocky shores or floating ice.</html>",
                "<html><b>Snowshoe Hare</b><br>This small mammal is well adapted to Nova Scotia’s seasons, with fur that changes from brown in summer to white in winter for camouflage. It is a vital prey species for many predators.</html>"
        };

        String[] animalImages = {
                "src/main/resources/NovaScotia/deer.jpg",
                "src/main/resources/NovaScotia/eagle.jpg",
                "src/main/resources/NovaScotia/coyote.jpg",
                "src/main/resources/NovaScotia/seal.jpeg",
                "src/main/resources/NovaScotia/snowshoe.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);

        }
        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        novascotiaFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(246, 245, 163));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        novascotiaFrame.add(animalsPanel, BorderLayout.CENTER);
        novascotiaFrame.add(readMoreButton, BorderLayout.SOUTH);

        novascotiaFrame.setVisible(true);
    }
    private void showManitobaAnimals(){
        JFrame ManitobaFrame = new JFrame("Top 5 Native Animals in Manitoba");
        ManitobaFrame.setSize(800, 750);
        ManitobaFrame.setLayout(new BorderLayout());

        Color lightBlue = new Color(230, 230, 230);
        ManitobaFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Plains Bison</b><br>One of the most iconic animals of the prairies, the plains bison was nearly driven to extinction but has been reintroduced in conservation areas like Riding Mountain National Park.</html>",
                "<html><b>Gray Wolf</b><br>Found in the boreal forests of Manitoba, gray wolves play a crucial role in the ecosystem by keeping prey populations in check.</html>",
                "<html><b>Wolverine</b><br>A fierce and elusive predator, the wolverine is known for its strength, endurance, and ability to survive in Manitoba’s harsh northern wilderness. Despite its small size, it can take down prey much larger than itself.</html>",
                "<html><b>Red Fox</b><br>Adaptable and widespread, the red fox thrives in a variety of habitats across Manitoba, from forests to urban areas.</html>",
                "<html><b>Great Gray Owl</b><br>Manitoba’s official provincial bird, this majestic owl is known for its silent flight and impressive hunting skills in the province’s forests.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Manitoba/bison.jpg",
                "src/main/resources/Manitoba/wolf.jpeg",
                "src/main/resources/Manitoba/wolverine.jpg",
                "src/main/resources/Manitoba/fox.png",
                "src/main/resources/Manitoba/owl.jpg"
        };
        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        ManitobaFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(237, 217, 180));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        ManitobaFrame.add(animalsPanel, BorderLayout.CENTER);
        ManitobaFrame.add(readMoreButton, BorderLayout.SOUTH);

        ManitobaFrame.setVisible(true);

    }
    private void showSaskatchewanAnimals(){
        JFrame SaskatchewanFrame = new JFrame("Top 5 Native Animals in Saskatchewan");
        SaskatchewanFrame.setSize(800, 750);
        SaskatchewanFrame.setLayout(new BorderLayout());

        Color lightBlue = new Color(230, 230, 230);
        SaskatchewanFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Pronghorn Antelope</b><br>North America's fastest land animal, reaching speeds of 88 km/h. Found in grasslands, it has sharp vision and sheds its horns annually.</html>",
                "<html><b>American Badger</b><br>A skilled burrower and fierce predator of rodents. It thrives in prairies and sometimes teams up with coyotes to hunt.</html>",
                "<html><b>Sharp-tailed Grouse</b><br>Saskatchewan’s provincial bird, known for its unique mating dances. Well-camouflaged and thrives in grasslands.</html>",
                "<html><b>Western Painted Turtle</b><br>A hardy freshwater turtle with a colorful shell. It hibernates underwater during Saskatchewan’s cold winters.</html>",
                "<html><b>Northern Pike</b><br>A large, aggressive freshwater fish with sharp teeth. Found in Saskatchewan’s lakes and rivers, it’s a top predator in its habitat.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Saskatchewan/antelope.jpg",
                "src/main/resources/Saskatchewan/badger.jpg",
                "src/main/resources/Saskatchewan/grouse.jpg",
                "src/main/resources/Saskatchewan/turtle.jpg",
                "src/main/resources/Saskatchewan/pike.jpg"
        };
        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        SaskatchewanFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(188, 241, 203));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        SaskatchewanFrame.add(animalsPanel, BorderLayout.CENTER);
        SaskatchewanFrame.add(readMoreButton, BorderLayout.SOUTH);

        SaskatchewanFrame.setVisible(true);

    }
    private void showAlbertaAnimals(){
        JFrame AlbertaFrame = new JFrame("Top 5 Native Animals in Alberta");
        AlbertaFrame.setSize(800, 750);
        AlbertaFrame.setLayout(new BorderLayout());

        Color lightBlue = new Color(230, 230, 230);
        AlbertaFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Grizzly Bear</b><br>A powerful predator found in Alberta’s forests and mountains. It hibernates in winter and has an excellent sense of smell for detecting food.</html>",
                "<html><b>Rocky Mountain Bighorn Sheep</b><br>Alberta’s provincial mammal, known for its massive curved horns. These sheep are expert climbers, thriving in rocky mountain slopes.</html>",
                "<html><b>Elk (Wapiti)</b><br>A large deer species that roams Alberta’s grasslands and forests. Males are famous for their loud bugling calls during mating season.</html>",
                "<html><b>Bull Trout</b><br>Alberta’s official fish, living in cold, clear mountain streams. Unlike some trout, it doesn’t have spots on its dorsal fin.</html>",
                "<html><b>Great Horned Owl</b><br>Alberta’s provincial bird, known for its piercing yellow eyes and tufted “horns.” A skilled nocturnal hunter that preys on small mammals.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Alberta/bear.jpg",
                "src/main/resources/Alberta/sheep.jpg",
                "src/main/resources/Alberta/elk.jpg",
                "src/main/resources/Alberta/trout.jpg",
                "src/main/resources/Alberta/owl.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        AlbertaFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(163, 246, 192));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        AlbertaFrame.add(animalsPanel, BorderLayout.CENTER);
        AlbertaFrame.add(readMoreButton, BorderLayout.SOUTH);

        AlbertaFrame.setVisible(true);

    }
    private void showBritishColumbiaAnimals(){
        JFrame BritishColumbiaFrame = new JFrame("Top 5 Native Animals in British Columbia");
        BritishColumbiaFrame.setSize(800, 750);
        BritishColumbiaFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        BritishColumbiaFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Vancouver Island Marmot</b><br>One of the rarest mammals in the world, this marmot is only found in BC’s mountains. Conservation efforts have helped its population recover.</html>",
                "<html><b>Spirit Bear (Kermode Bear)</b><br>A rare white-coated black bear found in BC’s coastal rainforests. It’s a symbol of conservation and Indigenous legends.</html>",
                "<html><b>Pacific Salmon</b><br>A vital species for BC’s ecosystem and Indigenous cultures. These fish migrate upstream to spawn, providing food for bears, eagles, and humans alike.</html>",
                "<html><b>Mountain Lion (Cougar)</b><br>The largest wild cat in Canada, known for its stealth and agility. BC has one of the highest cougar populations in North America.</html>",
                "<html><b>Sea Otter</b><br>A playful marine mammal that lives along BC’s coast. Sea otters help maintain kelp forests by keeping sea urchin populations in check.</html>"
        };

        String[] animalImages = {
                "src/main/resources/BritishColumbia/marmot.jpg",
                "src/main/resources/BritishColumbia/bear.jpg",
                "src/main/resources/BritishColumbia/salmon.jpg",
                "src/main/resources/BritishColumbia/lion.jpg",
                "src/main/resources/BritishColumbia/seaotter.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        BritishColumbiaFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(246, 163, 163));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        BritishColumbiaFrame.add(animalsPanel, BorderLayout.CENTER);
        BritishColumbiaFrame.add(readMoreButton, BorderLayout.SOUTH);

        BritishColumbiaFrame.setVisible(true);

    }
    private void showYukonAnimals(){
        JFrame YukonFrame = new JFrame("Top 5 Native Animals in Nunavut");
        YukonFrame.setSize(800, 750);
        YukonFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        YukonFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Canada Goose</b><br>A migratory bird known for its loud honking and V-shaped flight formations, commonly seen in Yukon’s wetlands and lakes.</html>",
                "<html><b>Dall Sheep</b><br>White-coated mountain dwellers with impressive curled horns, expertly navigating Yukon’s steep and rocky terrain.</html>",
                "<html><b>Arctic Ground Squirrel</b><br>A small but tough rodent that hibernates for most of the year, surviving extreme cold by lowering its body temperature below freezing.</html>",
                "<html><b>Peregrine Falcon</b><br>One of the world’s fastest birds, capable of reaching speeds over 300 km/h when diving to catch prey.</html>",
                "<html><b>Raven</b><br>Highly intelligent and adaptable, ravens are social birds that play an important role in Indigenous stories and Arctic ecosystems.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Yukon/goose.jpg",
                "src/main/resources/Yukon/sheep.jpg",
                "src/main/resources/Yukon/squirrel.jpg",
                "src/main/resources/Yukon/falcon.jpg",
                "src/main/resources/Yukon/raven.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        YukonFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(163, 187, 246));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        YukonFrame.add(animalsPanel, BorderLayout.CENTER);
        YukonFrame.add(readMoreButton, BorderLayout.SOUTH);

        YukonFrame.setVisible(true);
    }
    private void showNorthwestTerritoriesAnimals(){
        JFrame NorthwestTerritoriesFrame = new JFrame("Top 5 Native Animals in Northwest Territories");
        NorthwestTerritoriesFrame.setSize(800, 750);
        NorthwestTerritoriesFrame.setLayout(new BorderLayout());

        Color lightBlue = new Color(230, 230, 230);
        NorthwestTerritoriesFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Barren-ground Caribou (Rangifer tarandus groenlandicus)</b><br>A migratory caribou species that roams the tundra in large herds, essential to Indigenous communities for food and clothing.</html>",
                "<html><b>Whooping Crane (Grus americana)</b><br>One of the rarest birds in North America, whooping cranes nest in the wetlands of the Northwest Territories. Conservation efforts have helped, but they remain endangered.</html>",
                "<html><b>Muskox (Ovibos moschatus)</b><br>A shaggy-coated mammal adapted to the Arctic cold, muskoxen are known for their thick fur and social herd behavior.</html>",
                "<html><b>Arctic Grayling (Thymallus arcticus)</b><br>A freshwater fish found in cold rivers and lakes, recognizable by its large, sail-like dorsal fin.</html>",
                "<html><b>Eider</b><br>The Common Eider is a large sea duck found along northern coasts, known for its black-and-white plumage. It thrives in cold, coastal environments, feeding on marine invertebrates.</html>"
        };

        String[] animalImages = {
                "src/main/resources/NorthwestTerritories/barren.jpg",
                "src/main/resources/NorthwestTerritories/crane.jpeg",
                "src/main/resources/NorthwestTerritories/muskox.jpeg",
                "src/main/resources/NorthwestTerritories/grayling.jpg",
                "src/main/resources/NorthwestTerritories/eider.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }
        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        NorthwestTerritoriesFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(182, 191, 248));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        NorthwestTerritoriesFrame.add(animalsPanel, BorderLayout.CENTER);
        NorthwestTerritoriesFrame.add(readMoreButton, BorderLayout.SOUTH);

        NorthwestTerritoriesFrame.setVisible(true);
    }
    private void showNewBrunswickAnimals(){
        JFrame NreBrunswickFrame = new JFrame("Top 5 Native Animals in New Brunswick");
        NreBrunswickFrame.setSize(800, 750);
        NreBrunswickFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        NreBrunswickFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Eastern Chipmunk</b><br>A small, striped rodent that is often seen foraging for nuts and seeds. It hibernates during the winter and is commonly found in forests, gardens, and parks.</html>",
                "<html><b>Northern Flying Squirrel</b><br>This nocturnal squirrel is known for gliding through the air with the help of a membrane that stretches between its forelegs and hindlegs. It prefers mature forests for its habitat.</html>",
                "<html><b>Mink</b><br>A semi-aquatic mammal, the mink is known for its sleek dark fur and strong swimming abilities. It can be found along rivers, lakes, and marshes, preying on small animals and fish.</html>",
                "<html><b>Spotted Salamander</b><br>A large, brightly colored amphibian with black skin dotted with yellow or orange spots. It thrives in forested areas near vernal pools, where it breeds in the spring.</html>",
                "<html><b>Barred Owl</b><br>A medium-sized owl with distinctive horizontal bars across its chest. It is known for its hooting call and typically resides in dense forests, hunting small mammals and birds.</html>"
        };

        String[] animalImages = {
                "src/main/resources/NewBrunswick/chipmunk.jpeg",
                "src/main/resources/NewBrunswick/squirrel.jpg",
                "src/main/resources/NewBrunswick/mink.jpg",
                "src/main/resources/NewBrunswick/salamander.jpg",
                "src/main/resources/NewBrunswick/owl.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        NreBrunswickFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(185, 225, 192));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        NreBrunswickFrame.add(animalsPanel, BorderLayout.CENTER);
        NreBrunswickFrame.add(readMoreButton, BorderLayout.SOUTH);

        NreBrunswickFrame.setVisible(true);

    }
    private void showPrinceEdwardIslandAnimals(){
        JFrame NunavutFrame = new JFrame("Top 5 Native Animals in Prince Edward Island");
        NunavutFrame.setSize(800, 750);
        NunavutFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        NunavutFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Northern Shrike</b><br>The Northern Shrike is a predatory songbird known for its habit of impaling its prey on thorns or barbed wire. Found across PEI's fields and open woodlands, it hunts small mammals, birds, and insects, and is especially visible during the colder months.</html>",
                "<html><b>Island Shrew</b><br>A small, insectivorous mammal, the Island Shrew is native to the woodlands and meadows of Prince Edward Island. Its high metabolism allows it to be active year-round, foraging for insects, worms, and other small creatures.</html>",
                "<html><b>Great Blue Heron</b><br>A large, elegant bird often seen in PEI's coastal wetlands. Known for its long legs and distinctive blue-gray feathers, the Great Blue Heron is a skilled fisherman that feeds on fish, amphibians, and small mammals.</html>",
                "<html><b>Eastern Box Turtle</b><br>This turtle is known for its distinct, domed shell and its ability to completely close its shell for protection. It is primarily found in the woodlands of PEI and is an important part of the island's biodiversity.</html>",
                "<html><b>PEI Jumping Spider</b><br>A unique species of jumping spider that thrives in the grasslands and forests of Prince Edward Island. It is known for its excellent vision and acrobatic hunting abilities, often preying on insects much larger than itself.</html>"
        };

        String[] animalImages = {
                "src/main/resources/PrinceEdwardIsland/shrike.jpeg",
                "src/main/resources/PrinceEdwardIsland/shrew.jpg",
                "src/main/resources/PrinceEdwardIsland/heron.jpg",
                "src/main/resources/PrinceEdwardIsland/turtle.jpg",
                "src/main/resources/PrinceEdwardIsland/spider.jpeg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        NunavutFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(237, 232, 196));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        NunavutFrame.add(animalsPanel, BorderLayout.CENTER);
        NunavutFrame.add(readMoreButton, BorderLayout.SOUTH);

        NunavutFrame.setVisible(true);
    }
    private void showNewfoundlandandLabradorAnimals(){
        JFrame NewfoundlandFrame = new JFrame("Top 5 Native Animals in Newfoundland and Labrador");
        NewfoundlandFrame.setSize(800, 750);
        NewfoundlandFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        NewfoundlandFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Labrador Retriever</b><br>Famed worldwide, the Labrador Retriever is a breed of dog originating from the region. It was initially bred for retrieving fish and game, known for its intelligence and friendly demeanor.</html>",
                "<html><b>Northern Fur Seal</b><br>This species is found in the waters off Newfoundland and Labrador. Northern Fur Seals are known for their thick fur and agility in the water, where they hunt fish and squid.</html>",
                "<html><b>Pine Marten</b><br>A small, carnivorous mammal found in the forests of Newfoundland and Labrador. It is elusive, with a dark brown fur coat, and preys on small mammals, birds, and insects.</html>",
                "<html><b>Caribbean Sea Star</b><br>Found in the coastal waters of Newfoundland and Labrador, the Caribbean Sea Star is a brightly colored starfish. While its name suggests warmer waters, it's found in colder regions during specific migratory patterns.</html>",
                "<html><b>Minke Whale</b><br>One of the smaller baleen whales, the Minke Whale can be spotted off the coast of Newfoundland and Labrador. It is known for its distinctive black-and-white coloration and is often seen feeding in the region's rich waters.</html>"
        };

        String[] animalImages = {
                "src/main/resources/NewfoundlandandLabrador/dog.jpg",
                "src/main/resources/NewfoundlandandLabrador/seal.jpeg",
                "src/main/resources/NewfoundlandandLabrador/marten.jpg",
                "src/main/resources/NewfoundlandandLabrador/star.jpg",
                "src/main/resources/NewfoundlandandLabrador/whale.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        NewfoundlandFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(163, 231, 246));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        NewfoundlandFrame.add(animalsPanel, BorderLayout.CENTER);
        NewfoundlandFrame.add(readMoreButton, BorderLayout.SOUTH);

        NewfoundlandFrame.setVisible(true);
    }
    private void showNunavutAnimals() {
        JFrame NunavutFrame = new JFrame("Top 5 Native Animals in Nunavut");
        NunavutFrame.setSize(800, 750);
        NunavutFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        NunavutFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {
                "<html><b>Polar Bear</b><br>Known for its white fur and strong build, the polar bear is an iconic species of the Arctic. They are found primarily on the sea ice and are known for being skilled hunters, primarily preying on seals.</html>",
                "<html><b>Caribou</b><br>A large herbivorous mammal, the caribou is well-adapted to the harsh Arctic conditions. They are often seen in large herds and play a key role in the local ecosystem.</html>",
                "<html><b>Arctic Fox</b><br>The Arctic fox is a small mammal with a thick coat that changes color with the seasons, from white in winter to brown in summer. It survives the cold winters of Nunavut by hunting small mammals and scavenging.</html>",
                "<html><b>Snowy Owl</b><br>The snowy owl is a large, white owl that is found in the tundra regions. It has excellent hunting skills, preying on lemmings and other small animals.</html>",
                "<html><b>Beluga Whale</b><br>These small white whales are found in the Arctic and sub-Arctic waters. They are highly social creatures, often seen in pods, and are well adapted to the cold waters.</html>"
        };

        String[] animalImages = {
                "src/main/resources/Nunavut/bear.jpeg",
                "src/main/resources/Nunavut/caribou.jpg",
                "src/main/resources/Nunavut/fox.jpg",
                "src/main/resources/Nunavut/owl.jpeg",
                "src/main/resources/Nunavut/whale.jpg"
        };

        for (int i = 0; i < animals.length; i++) {
            JPanel animalPanel = new JPanel(new BorderLayout());
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

            // Image Label
            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // Space between image & text

            // Name & Description Label
            JLabel animalLabel = new JLabel(animals[i]);
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        NunavutFrame.add(scrollPane, BorderLayout.CENTER);


        // Add "Read More" Button
        JButton readMoreButton = new JButton("Read More");
        readMoreButton.setBackground(new Color(163, 246, 236));
        readMoreButton.addActionListener(e -> openDetailedNunavutFrame());

        NunavutFrame.add(animalsPanel, BorderLayout.CENTER);
        NunavutFrame.add(readMoreButton, BorderLayout.SOUTH);

        NunavutFrame.setVisible(true);
    }

    // Table for Nunavut
    private void openDetailedNunavutFrame() {
        // Adjusted frame size for better visibility
        JFrame detailedFrame = new JFrame("Detailed Information on Nunavut Wildlife");
        detailedFrame.setSize(1200, 700);  // Make the frame larger
        detailedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the layout to BoxLayout for better vertical distribution
        detailedFrame.setLayout(new BoxLayout(detailedFrame.getContentPane(), BoxLayout.Y_AXIS));

        AnimalInfo polarBear = new AnimalInfo(
                "Polar Bear", 350, 250, 25, 30,
                "Historical Population: Estimated 5,000 in 1980",
                "Current Population: Approx 26,000", 4.0
        );

        AnimalInfo caribou = new AnimalInfo(
                "Caribou", 320, 250, 15, 18,
                "Historical Population: 100,000 in 1980",
                "Current Population: Approx 300,000", 5.0
        );

        AnimalInfo arcticFox = new AnimalInfo(
                "Arctic Fox", 4.5, 3.5, 10, 12,
                "Historical Population: Estimated 250,000 in 1980",
                "Current Population: Approx 500,000", 6.0
        );

        AnimalInfo snowyOwl = new AnimalInfo(
                "Snowy Owl", 2.5, 2.3, 10, 12,
                "Historical Population: Estimated 20,000 in 1980",
                "Current Population: Approx 28,000", 3.0
        );

        AnimalInfo belugaWhale = new AnimalInfo(
                "Beluga Whale", 1500, 1350, 40, 50,
                "Historical Population: Estimated 100,000 in 1980",
                "Current Population: Approx 120,000", 2.5
        );

        // Table Headers
        String[] columnNames = {
                "Animal", "Mass (Male) kg", "Mass (Female) kg",
                "Lifespan (Male) Years", "Lifespan (Female) Years",
                "Historical Population", "Current Population", "Population Growth (%)"
        };

        Object[][] data = {
                {polarBear.getName(), polarBear.getMassMale(), polarBear.getMassFemale(),
                        polarBear.getLifespanMale(), polarBear.getLifespanFemale(),
                        polarBear.getHistoricalPopulation(), polarBear.getCurrentPopulation(),
                        polarBear.getPopulationGrowth()},
                {caribou.getName(), caribou.getMassMale(), caribou.getMassFemale(),
                        caribou.getLifespanMale(), caribou.getLifespanFemale(),
                        caribou.getHistoricalPopulation(), caribou.getCurrentPopulation(),
                        caribou.getPopulationGrowth()},
                {arcticFox.getName(), arcticFox.getMassMale(), arcticFox.getMassFemale(),
                        arcticFox.getLifespanMale(), arcticFox.getLifespanFemale(),
                        arcticFox.getHistoricalPopulation(), arcticFox.getCurrentPopulation(),
                        arcticFox.getPopulationGrowth()},
                {snowyOwl.getName(), snowyOwl.getMassMale(), snowyOwl.getMassFemale(),
                        snowyOwl.getLifespanMale(), snowyOwl.getLifespanFemale(),
                        snowyOwl.getHistoricalPopulation(), snowyOwl.getCurrentPopulation(),
                        snowyOwl.getPopulationGrowth()},
                {belugaWhale.getName(), belugaWhale.getMassMale(), belugaWhale.getMassFemale(),
                        belugaWhale.getLifespanMale(), belugaWhale.getLifespanFemale(),
                        belugaWhale.getHistoricalPopulation(), belugaWhale.getCurrentPopulation(),
                        belugaWhale.getPopulationGrowth()}
        };

        // Create JTable (same as before)
        JTable animalTable = new JTable(data, columnNames);
        animalTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(animalTable);

        // Create Population Growth Chart (Updated chart size)
        JFreeChart lineChart = populationGrowthChartNunavut();
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(1000, 500)); // Adjusted chart height to fit well

        // Panel to hold both table and graph (use BoxLayout to stack them vertically)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(scrollPane);  // Add the table
        mainPanel.add(chartPanel);  // Add the chart

        // Add to Frame
        detailedFrame.add(mainPanel, BorderLayout.CENTER);
        detailedFrame.setVisible(true);
    }

    //Graph for Nunavut
    private JFreeChart populationGrowthChartNunavut() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Population growth data (same as before)
        dataset.addValue(5000, "Polar Bear", "1980");
        dataset.addValue(12000, "Polar Bear", "1990");
        dataset.addValue(18000, "Polar Bear", "2000");
        dataset.addValue(26000, "Polar Bear", "2020");

        dataset.addValue(100000, "Caribou", "1980");
        dataset.addValue(150000, "Caribou", "1990");
        dataset.addValue(200000, "Caribou", "2000");
        dataset.addValue(300000, "Caribou", "2020");

        dataset.addValue(250000, "Arctic Fox", "1980");
        dataset.addValue(320000, "Arctic Fox", "1990");
        dataset.addValue(400000, "Arctic Fox", "2000");
        dataset.addValue(500000, "Arctic Fox", "2020");

        dataset.addValue(20000, "Snowy Owl", "1980");
        dataset.addValue(22000, "Snowy Owl", "1990");
        dataset.addValue(25000, "Snowy Owl", "2000");
        dataset.addValue(28000, "Snowy Owl", "2020");

        dataset.addValue(100000, "Beluga Whale", "1980");
        dataset.addValue(110000, "Beluga Whale", "1990");
        dataset.addValue(115000, "Beluga Whale", "2000");
        dataset.addValue(120000, "Beluga Whale", "2020");

        // Create Line Chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Nunavut Animal Population Growth", // Title
                "Year", "Population", // X and Y axis labels
                dataset
        );

        // Create ChartPanel with adjusted size (taller height)
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1000, 600));

        return chart;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
