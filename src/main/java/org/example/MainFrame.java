
package org.example;

import com.opencsv.CSVWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import static org.example.dataReader.getConservationStatuses;

public class MainFrame extends JFrame {

    private JPanel statusPanel, mapPanel;
    private JButton viewMapButton;
    private JButton loginButton;
    private JLayeredPane layeredPane;
    private JLabel mapLabel;
    private String savedUsername = null; // Store the username temporarily
    private JTextField searchField;
    private JPanel rightSidebar; // Panel for search results
    private static final String USER_DATA_FILE = "src/main/resources/users.csv";

    private static final String NEWSLETTER_SUBSCRIPTIONS_FILE = "src/main/resources/newsletter_subscriptions.csv";
    private String currentUsername = null;





    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Canadian Wildlife");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Top Panel (Header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(32, 33, 34));
        topPanel.setPreferredSize(new Dimension(getWidth(), 70)); // Fixed height for the header

        JLabel wildlifePortalLabel = new JLabel("Wildlife Information Portal", SwingConstants.CENTER);
        wildlifePortalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        wildlifePortalLabel.setForeground(Color.WHITE);
        wildlifePortalLabel.setBorder(BorderFactory.createEmptyBorder(20, 230, 20, 20)); // Padding

        // Create a panel with the same width as the map panel and center the label
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setOpaque(false);

        labelPanel.setPreferredSize(new Dimension(100, 70));
        labelPanel.add(wildlifePortalLabel);

        // Add the label panel to the top panel
        topPanel.add(labelPanel, BorderLayout.CENTER);

        // Search Bar Panel (aligned to the right side panel)
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(new Color(32, 33, 34)); // Match the header color
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 10));

        // Search Text Field
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(170, 30));  // Width is constrained to the right panel's width
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBackground(new Color(89, 91, 92));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            if (!searchQuery.isEmpty()) {
                displaySearchResults(searchQuery);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "Search Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add search components to the search bar panel
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);

        // Add the search bar panel to the top panel, aligning it to the east (right)
        topPanel.add(searchBarPanel, BorderLayout.EAST);

        // Add the top panel to the frame
        add(topPanel, BorderLayout.NORTH);

        // Right Sidebar (Search Results Panel)
        rightSidebar = new JPanel();
        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
        rightSidebar.setBackground(new Color(222, 224, 227));
        rightSidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        rightSidebar.setPreferredSize(new Dimension(240, getHeight())); // Fixed width

        // Add a placeholder label for search results
        JLabel searchPlaceholder = new JLabel("Search results will appear here.", SwingConstants.CENTER);
        searchPlaceholder.setFont(new Font("Arial", Font.ITALIC, 14));
        searchPlaceholder.setForeground(Color.DARK_GRAY);
        rightSidebar.add(searchPlaceholder);

        add(rightSidebar, BorderLayout.EAST);

        // Sidebar (Sleek Dark Gray)
        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS)); // Vertical layout
        statusPanel.setBackground(new Color(45, 45, 45));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        statusPanel.setPreferredSize(new Dimension(200, getHeight() - 70)); // Adjust height to account for header
        add(statusPanel, BorderLayout.WEST);

        // Initially only "View Map" and "Log In" buttons
        viewMapButton = createStyledButton("View Map");
        loginButton = createStyledButton("Log In");

        loginButton.addActionListener(evt -> loginActionPerformed());
        assert viewMapButton != null;
        viewMapButton.addActionListener(evt -> loadMap());

        // Add buttons to sidebar
        updateSidebarButtons(false);

        // Map Panel
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Load the image
                ImageIcon mapIcon = new ImageIcon("src/main/resources/homepage_banner.jpg");
                Image image = mapIcon.getImage();

                // Draw the image to fit the panel dynamically
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mapPanel.setLayout(new BorderLayout());

        add(mapPanel, BorderLayout.CENTER);

        // Load Image
        ImageIcon mapIcon = new ImageIcon("src\\main\\resources\\homepage_banner.jpg");
        Image scaledImage = mapIcon.getImage().getScaledInstance(830, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Add Image to JLabel
        mapLabel = new JLabel(scaledIcon, SwingConstants.CENTER);

        // Layered Pane
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        mapLabel.setBounds(0, 0, 800, 600);
        layeredPane.add(mapLabel, Integer.valueOf(0));

        mapPanel.add(layeredPane, BorderLayout.CENTER);
        add(mapPanel, BorderLayout.CENTER);

        setVisible(true);
    }
    private void openNewsletterSubscriptionForm() {
        JFrame newsletterFrame = new JFrame("Subscribe to Newsletter");
        newsletterFrame.setSize(500, 350);
        newsletterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newsletterFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Form fields
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel interestsLabel = new JLabel("Interests:");
        String[] interestOptions = {
                "General Wildlife News",
                "Conservation Updates",
                "Animal Spotting Alerts",
                "All Topics"
        };
        JComboBox<String> interestsCombo = new JComboBox<>(interestOptions);

        // Add components to form
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(interestsLabel);
        formPanel.add(interestsCombo);

        // Submit button
        JButton subscribeButton = new JButton("Subscribe");
        subscribeButton.setBackground(new Color(70, 130, 180));
        subscribeButton.setForeground(Color.WHITE);
        subscribeButton.addActionListener(e -> {
            if (validateNewsletterForm(nameField, emailField)) {
                if (saveNewsletterSubscription(
                        nameField.getText(),
                        emailField.getText(),
                        (String) interestsCombo.getSelectedItem()
                )) {
                    JOptionPane.showMessageDialog(newsletterFrame,
                            "Thank you for subscribing to our newsletter!",
                            "Subscription Successful", JOptionPane.INFORMATION_MESSAGE);
                    newsletterFrame.dispose();
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(subscribeButton);

        mainPanel.add(new JLabel("<html><h2>Newsletter Subscription</h2></html>"), BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        newsletterFrame.add(mainPanel);
        newsletterFrame.setVisible(true);
    }

    private boolean validateNewsletterForm(JTextField nameField, JTextField emailField) {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean saveNewsletterSubscription(String name, String email, String interests) {
        try {
            File file = new File(NEWSLETTER_SUBSCRIPTIONS_FILE);
            boolean fileExists = file.exists();

            // Create parent directories if they don't exist
            file.getParentFile().mkdirs();

            try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
                // Write header if file is new
                if (!fileExists) {
                    writer.writeNext(new String[]{"Timestamp", "Name", "Email", "Interests"});
                }

                // Write data
                String[] data = {
                        new java.util.Date().toString(),
                        name,
                        email,
                        interests
                };
                writer.writeNext(data);
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving subscription: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private JFrame openArticlesFrame() {
        JFrame articlesFrame = new JFrame("Articles");
        articlesFrame.setSize(600, 450);
        articlesFrame.setLocationRelativeTo(null);  // Center the frame

        // Main panel with background color and padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(182, 179, 179));

        // Introductory label
        JLabel introLabel = new JLabel("Explore the articles below by clicking on the title to get started.", JLabel.CENTER);
        introLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        introLabel.setForeground(new Color(50, 50, 50));
        introLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Article panel with grid layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));  // Flexible layout with spacing
        panel.setBackground(new Color(182, 179, 179));  // Light grey background
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));  // Add padding

        String[][] articles = {
                {"Climate Change & Wildlife", "https://www.wwf.org"},
                {"Endangered Species in Canada", "https://www.canada.ca"},
                {"Wildlife Protection Efforts", "https://www.nationalgeographic.com"},
                {"Impact of Deforestation", "https://www.un.org/sustainabledevelopment"},
                {"How You Can Help Wildlife", "https://www.worldwildlife.org/get-involved"}
        };

        for (String[] article : articles) {
            JButton linkButton = new JButton(article[0]);

            // Button styling
            linkButton.setFont(new Font("Arial", Font.BOLD, 14));
            linkButton.setBackground(new Color(81, 81, 81));  // Dark grey
            linkButton.setForeground(Color.WHITE);
            linkButton.setFocusPainted(false);
            linkButton.setBorder(BorderFactory.createRaisedBevelBorder());

            // Hover effect
            linkButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    linkButton.setBackground(new Color(65, 66, 67));  // Darker grey on hover
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    linkButton.setBackground(new Color(81, 81, 81));  // Original color
                }
            });

            // Open webpage action
            linkButton.addActionListener(e -> openWebpage(article[1]));
            panel.add(linkButton);
        }

        JScrollPane scrollPane = new JScrollPane(panel);  // Add scrolling support
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add components to the main panel
        mainPanel.add(introLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        articlesFrame.add(mainPanel);
        articlesFrame.setVisible(true);

        return articlesFrame;
    }

    private void openConservationStatusFrame() {
        JFrame restrictedHuntingFrame = new JFrame("Hunting Periods and Limits");
        restrictedHuntingFrame.setSize(850, 330);
        restrictedHuntingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        restrictedHuntingFrame.setLocationRelativeTo(null); // Center the window
        restrictedHuntingFrame.setResizable(false);

        List<String[]> conservationStatuses = getConservationStatuses();

        Map<String, List<String>> sortedAnimalsByStatus = new HashMap<>();

        // convert a list of animals and their conservation statuses to a map of conservation statuses
        // and animals that belong to that status
        for (String[] entry : conservationStatuses) {
            String animal = entry[0];
            String status = entry[1];

            sortedAnimalsByStatus.putIfAbsent(status, new ArrayList<>());
            sortedAnimalsByStatus.get(status).add(animal);
        }

        // get sizes of each category
        int numLeastConcern = sortedAnimalsByStatus.get("Least Concern").size();
        int numNearThreatened = sortedAnimalsByStatus.get("Near Threatened").size();
        int numVulnerable = sortedAnimalsByStatus.get("Vulnerable").size();
        int numEndangered = sortedAnimalsByStatus.get("Endangered").size();

        int maxSize = Math.max(Math.max(numLeastConcern, numNearThreatened),
                Math.max(numVulnerable, numEndangered));

        Object[][] data = new Object[maxSize][4];

        // convert map to 2d array that can be displayed by JTable
        for (int i = 0; i < maxSize; i++) {
            data[i][0] = (i < numLeastConcern) ? sortedAnimalsByStatus.get("Least Concern").get(i) : "";
            data[i][1] = (i < numNearThreatened) ? sortedAnimalsByStatus.get("Near Threatened").get(i) : "";
            data[i][2] = (i < numVulnerable) ? sortedAnimalsByStatus.get("Vulnerable").get(i) : "";
            data[i][3] = (i < numEndangered) ? sortedAnimalsByStatus.get("Endangered").get(i) : "";
        }

        String[] columns = {"Least Concern", "Near Threatened", "Vulnerable", "Endangered"};

        // Create table
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.GRAY);
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);

        // Center-align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(25, 25, 25));
        header.setForeground(Color.WHITE);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        restrictedHuntingFrame.add(scrollPane);

        // Show frame
        restrictedHuntingFrame.setVisible(true);
    }

    private void openRestrictedHuntingFrame() {
        JFrame restrictedHuntingFrame = new JFrame("Hunting Periods and Limits");
        restrictedHuntingFrame.setSize(850, 330);
        restrictedHuntingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        restrictedHuntingFrame.setLocationRelativeTo(null); // Center the window
        restrictedHuntingFrame.setResizable(false);

        String[] columns = {"Species", "Season", "Quantity", "Limit Period"};
        Object[][] data = {
                {"Muskox", "August 12 - October 17", "2", "Season"},
                {"Northern Pike", "May 1 - November 26", "2", "Day"},
                {"Pacific Salmon", "June 6 - October 3", "3", "Day"},
                {"Caribou", "August 28 - October 27", "1", "Season"},
                {"Snowshoe Hare", "September 12 - February 10", "6", "Day"},
                {"Sharp-tailed Grouse", "October 14 - December 18", "4", "Day"},
                {"Arctic Ground Squirrel", "Year Round", "No limit", "No limit"},
                {"Beaver", "October 20 - April 3", "10", "Season"},
                {"Bull Trout", "May 17 - October 2", "Catch and Release(0)", "Catch and Release(0)"},
                {"Harlequin Duck", "October 20 - January 10", "1", "Day"}
        };

        // Create table
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.GRAY);
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);


        // Center-align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(25, 25, 25));
        header.setForeground(Color.WHITE);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        restrictedHuntingFrame.add(scrollPane);

        // Show frame
        restrictedHuntingFrame.setVisible(true);
    }

    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to open the link!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(false);  // No background
        button.setContentAreaFilled(false);  // No background fill
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));  // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect: light background to indicate clickable area
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setOpaque(true);
                button.setBackground(new Color(60, 60, 60)); // Slightly lighter gray hover effect
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setOpaque(false);  // Return to invisible
                button.setBackground(null);  // Remove background color
            }
        });

        return button;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Button Hover Effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bg.darker()); // Darken on hover
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bg); // Revert on exit
            }
        });

        return button;
    }

    // Update Sidebar Buttons (Initial or After Login)
    private void updateSidebarButtons(boolean loggedIn) {
        statusPanel.removeAll();

        int sidebarWidth = statusPanel.getPreferredSize().width;
        if (loggedIn) {
            // Buttons to be shown after login
            String[] loggedInLabels = {
                    "Subscribe to Newsletter",
                    "View Articles",
                    "Conservation Statuses",
                    "Restricted Hunting",
                    "Animal Facts",
                    "Emergency Contact",
                    "View Map"
            };

            for (String label : loggedInLabels) {
                JButton button = createStyledButton(label);
                button.setMaximumSize(new Dimension(sidebarWidth, 40));
                button.setAlignmentX(Component.LEFT_ALIGNMENT);

                switch (label) {
                    case "View Articles" -> button.addActionListener(e -> openArticlesFrame());
                    case "Conservation Statuses" -> button.addActionListener(e -> openConservationStatusFrame());
                    case "Restricted Hunting" -> button.addActionListener(e -> openRestrictedHuntingFrame());
                    case "Animal Facts" -> button.addActionListener(e -> openAnimalFactsFrame());
                    case "Emergency Contact" -> button.addActionListener(e -> openContactInfo());
                    case "Subscribe to Newsletter" -> button.addActionListener(e -> openNewsletterSubscriptionForm());
                    case "View Map" -> button.addActionListener(e -> loadMap());
                }

                statusPanel.add(button);
            }

            JButton logoutButton = createStyledButton("Log Out", new Color(45, 45, 45), Color.RED);
            logoutButton.setMaximumSize(new Dimension(sidebarWidth, 40));
            logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            logoutButton.addActionListener(e -> logoutActionPerformed());
            statusPanel.add(logoutButton);
        } else {
            loginButton.setMaximumSize(new Dimension(sidebarWidth, 40));
            loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            statusPanel.add(loginButton);

            JButton signInButton = createStyledButton("Sign Up", new Color(45, 45, 45), Color.WHITE);
            signInButton.setMaximumSize(new Dimension(sidebarWidth, 40));
            signInButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            signInButton.addActionListener(evt -> signInActionPerformed());
            statusPanel.add(signInButton);

            viewMapButton.setMaximumSize(new Dimension(sidebarWidth, 40));
            viewMapButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            statusPanel.add(viewMapButton);
        }

        statusPanel.revalidate();
        statusPanel.repaint();
    }
    private void openAnimalFactsFrame() {
        // Create a new frame with a list of animals
        JFrame animalFrame = new JFrame("Animal Facts");
        animalFrame.setSize(400, 500);  // Increased size to better fit all buttons
        animalFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10)); // Using GridLayout for a cleaner layout with two columns
        panel.setBackground(new Color(83, 83, 83));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Array of animals and their facts
        String[] animals = {
                "Moose", "Beaver", "Eastern Wolf", "Common Loon", "Blanding's Turtle", "Canada Lynx", "Atlantic Puffin",
                "Grizzly Bear", "Snowy Owl", "Peregrine Falcon", "Red Fox", "White-tailed Deer", "Northern Cardinal",
                "Black Bear", "Great Blue Heron", "Wild Turkey", "Eastern Chipmunk", "Cougar"
        };

        // Create buttons for each animal and add to panel
        for (String animal : animals) {
            JButton animalButton = createStyledButton(animal);
            animalButton.addActionListener(this::animalFactActionPerformed);
            panel.add(animalButton);
        }

        // ScrollPane to handle overflow in case there are too many buttons
        JScrollPane scrollPane = new JScrollPane(panel);
        animalFrame.add(scrollPane);

        animalFrame.setVisible(true);
    }

    private void animalFactActionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        String animalName = sourceButton.getText(); // Get the name of the clicked animal

        String animalFact = getAnimalFact(animalName);

        // Create a new frame for displaying the fact
        JFrame factFrame = new JFrame("Animal Fact");
        factFrame.setSize(400, 250);
        factFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(197, 196, 196));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel factLabel = new JLabel("<html><div style='text-align: center;'>" + animalFact + "</div></html>");
        factLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        factLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton closeButton = new JButton("OK");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(95, 96, 97));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        closeButton.addActionListener(event -> factFrame.dispose());

        panel.add(factLabel, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        factFrame.add(panel);
        factFrame.setVisible(true);
    }

    private String getAnimalFact(String animalName) {
        Map<String, String> animalFacts = new HashMap<>();

        animalFacts.put("Moose", "The moose is the largest member of the deer family, known for its massive size and distinctive antlers.");
        animalFacts.put("Beaver", "Beavers are known for their incredible ability to build dams and lodges using their strong teeth.");
        animalFacts.put("Eastern Wolf", "The Eastern wolf is a subspecies of the grey wolf, native to Ontario's forests.");
        animalFacts.put("Common Loon", "Loons are known for their haunting calls and their ability to dive deep underwater.");
        animalFacts.put("Blanding's Turtle", "This turtle has a yellow throat and a domed shell, making it a distinctive and protected species.");
        animalFacts.put("Canada Lynx", "The Canada Lynx has large paws that help it walk on snow and thick fur to survive cold winters.");
        animalFacts.put("Atlantic Puffin", "The Atlantic Puffin, often called the 'clown of the sea,' has a colorful beak and strong swimming skills.");
        animalFacts.put("Grizzly Bear", "Grizzly bears are powerful predators, known for their strength and large size, and are often found in North American forests.");
        animalFacts.put("Snowy Owl", "Snowy owls are large, white owls native to the Arctic, known for their striking appearance and exceptional hunting skills.");
        animalFacts.put("Peregrine Falcon", "Peregrine falcons are the fastest birds in the world, capable of reaching speeds over 240 miles per hour in a dive.");
        animalFacts.put("Red Fox", "Red foxes are highly adaptable animals with distinctive red fur, known for their cunning nature and intelligence.");
        animalFacts.put("White-tailed Deer", "White-tailed deer are common in North America and are known for the white underside of their tails, which they flash as a warning sign.");
        animalFacts.put("Northern Cardinal", "Northern cardinals are vibrant red songbirds, known for their bright color and melodious songs.");
        animalFacts.put("Black Bear", "Black bears are the most common bear species in North America, typically smaller than their grizzly counterparts, but still formidable.");
        animalFacts.put("Great Blue Heron", "The great blue heron is a large wading bird, known for its graceful flight and striking blue-gray plumage.");
        animalFacts.put("Wild Turkey", "Wild turkeys are large birds native to North America, known for their impressive plumage and distinctive gobbling calls.");
        animalFacts.put("Eastern Chipmunk", "Eastern chipmunks are small, striped squirrels found in forests, known for their ability to gather and store food.");
        animalFacts.put("Cougar", "Cougars, also known as mountain lions or pumas, are large cats that roam the Americas and are skilled hunters and climbers.");


        return animalFacts.getOrDefault(animalName, "Fact not available for this animal.");
    }

    private void openContactInfo() {
        JFrame contactInfoFrame = new JFrame("Emergency Contact Information");
        JLabel contactInfoLabel = new JLabel("<html>Parks Canada Emergency Number:<br> 1-888-773-8888 <br><br> Canada General Inquiries number:<br> 1-866-677-7678 <br><br>Environment and Climate Change Canada:<br> 1-800-668-6767 </html> ", JLabel.CENTER);

        contactInfoFrame.getContentPane().setBackground(new Color(83, 83, 83));
        contactInfoLabel.setForeground(new Color(255, 255, 255));

        contactInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contactInfoFrame.add(contactInfoLabel);
        contactInfoFrame.setSize(300, 300);
        contactInfoFrame.setVisible(true);
    }

    private void logoutActionPerformed() {
        currentUsername = null;
        JOptionPane.showMessageDialog(this,
                "You have logged out.",
                "Log Out", JOptionPane.INFORMATION_MESSAGE);
        updateSidebarButtons(false);
    }
    // Sign In Functionality (Store the username temporarily)
    private void signInActionPerformed() {
        JPanel panel = new JPanel(new GridLayout(3,2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));



        int option = JOptionPane.showConfirmDialog(this, panel, "Sign Up", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this,"Username and password cannot be empty!", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(userExists(username)){
                JOptionPane.showMessageDialog(this,
                        "Username already exists!",
                        "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                return;

            }
            if(registerUser(username,password)){
                currentUsername = username;
                JOptionPane.showMessageDialog(this,
                        "Account created successfully!",
                        "Sign Up", JOptionPane.INFORMATION_MESSAGE);
                updateSidebarButtons(true);
            }else{
                JOptionPane.showMessageDialog(this,
                        "Failed to create account. Please try again.",
                        "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Login Functionality
    private void loginActionPerformed() {
        JPanel panel = new JPanel(new GridLayout(3,2));
        JTextField usernameField= new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        int option = JOptionPane.showConfirmDialog(this, panel, "Log In ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(option == JOptionPane.OK_OPTION){
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Username and password cannot be empty!",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(authenticateUser(username, password)){
                currentUsername = username;
                JOptionPane.showMessageDialog(this,
                        "Welcome back " + username + "!",
                        "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                updateSidebarButtons(true);
            }else{
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password!",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    private boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            // Skip header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error accessing user database",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    private boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            // Skip header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 &&
                        parts[0].equalsIgnoreCase(username) &&
                        parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error accessing user database",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    private boolean registerUser(String username, String password) {
        try {
            // Check if username already exists
            if (userExists(username)) {
                JOptionPane.showMessageDialog(this,
                        "Username already exists!",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Append new user to file
            try (FileWriter writer = new FileWriter(USER_DATA_FILE, true);
                 BufferedWriter bw = new BufferedWriter(writer);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println(username + "," + password);
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error creating account: " + e.getMessage(),
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    private List<String> loadAnimalNames() {
        List<String> animalNames = new ArrayList<>();
        String csvFile = "src/main/resources/wildlife_data.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Split the line by comma
                if (data.length > 1) {
                    String animalName = data[1].trim(); // The "Name" column in the CSV
                    animalNames.add(animalName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load animal data from CSV.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return animalNames;
    }
    private List<String> displaySearchResults(String searchQuery) {
        // Clear the existing content in the right sidebar
        rightSidebar.removeAll();
        rightSidebar.revalidate();
        rightSidebar.repaint();

        // List of provinces
        String[] provinces = {
                "Ontario", "Quebec", "Nova Scotia", "New Brunswick", "Manitoba",
                "Saskatchewan", "Alberta", "British Columbia", "Yukon",
                "Prince Edward Island", "Newfoundland and Labrador", "Northwest Territories", "Nunavut"
        };
        // Load animal names from the CSV file
        List<String> animalNames = loadAnimalNames();

        // Create a list to store search results
        List<String> results = new ArrayList<>();

        // Search through provinces
        for (String province : provinces) {
            if (province.toLowerCase().contains(searchQuery.toLowerCase())) {
                results.add("Province: " + province);
            }
        }
        // Search through animals
        for (String animal : animalNames) {
            if (animal.toLowerCase().contains(searchQuery.toLowerCase())) {
                results.add("Animal: " + animal);
            }
        }

        // Display the results in the right sidebar
        if (results.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No results found for: " + searchQuery);
            noResultsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            rightSidebar.add(noResultsLabel);
        } else {
            for (String result : results) {
                JButton resultButton = new JButton(result);
                resultButton.setFont(new Font("Arial", Font.PLAIN, 12));
                resultButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, resultButton.getPreferredSize().height)); // Make buttons full width
                resultButton.addActionListener(e -> handleSearchResultSelection(result));
                rightSidebar.add(resultButton);
            }
        }

        // Add some spacing at the bottom
        rightSidebar.add(Box.createVerticalGlue());

        // Refresh the sidebar
        rightSidebar.revalidate();
        rightSidebar.repaint();

        return results;
    }

    private void handleSearchResultSelection(String selectedItem) {
        String[] parts = selectedItem.split(": ");
        String type = parts[0]; // "Province"
        String name = parts[1]; // The name of the province

        // Handle the selection based on the type
        if (type.equals("Province")) {
            openProvinceDetails(name);
        }
    }

    private void openAnimalDetails(String name) {
        JOptionPane.showMessageDialog(this, "Animal details for: " + name, "Animal Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openProvinceDetails(String provinceName) {
        switch (provinceName) {
            case "Ontario" -> showOntarioAnimals();
            case "Quebec" -> showQuebecAnimals();
            case "Nova Scotia" -> showNovaScotiaAnimals();
            case "New Brunswick" -> showNewBrunswickAnimals();
            case "Manitoba" -> showManitobaAnimals();
            case "Saskatchewan" -> showSaskatchewanAnimals();
            case "Alberta" -> showAlbertaAnimals();
            case "British Columbia" -> showBritishColumbiaAnimals();
            case "Yukon" -> showYukonAnimals();
            case "Prince Edward Island" -> showPrinceEdwardIslandAnimals();
            case "Newfoundland and Labrador" -> showNewfoundlandandLabradorAnimals();
            case "Northwest Territories" -> showNorthwestTerritoriesAnimals();
            case "Nunavut" -> showNunavutAnimals();
        }
    }
    /**
     * Loads the map image and displays province markers.
     */
    private void loadMap() {
        ImageIcon mapIcon = new ImageIcon("src/main/resources/cad.jpg");
        Image scaledImage = mapIcon.getImage().getScaledInstance(mapPanel.getWidth(), mapPanel.getHeight(), Image.SCALE_SMOOTH);
        mapLabel.setIcon(new ImageIcon(scaledImage));

        int x = (mapPanel.getWidth() - mapLabel.getIcon().getIconWidth()) / 2;
        int y = (mapPanel.getHeight() - mapLabel.getIcon().getIconHeight()) / 2;
        mapLabel.setBounds(x, y, mapLabel.getIcon().getIconWidth(), mapLabel.getIcon().getIconHeight());

        // Add province buttons
        addProvinceButton("Ontario", 430, 405);
        addProvinceButton("Quebec", 520, 340);
        addProvinceButton("Nova Scotia", 655, 420);
        addProvinceButton("New Brunswick", 630, 405);
        addProvinceButton("Manitoba", 330, 370);
        addProvinceButton("Saskatchewan", 270, 380);
        addProvinceButton("Alberta", 200, 360);
        addProvinceButton("British Columbia", 120, 340);
        addProvinceButton("Yukon", 110, 210);
        addProvinceButton("Prince Edward Island", 650, 390);
        addProvinceButton("Newfoundland and Labrador", 610, 310);
        addProvinceButton("Northwest Territories", 190, 230);
        addProvinceButton("Nunavut", 325, 240);

        JLabel instructionLabel = new JLabel("<html><b>Click on the Green Flags to View Animals of That Province \uD83D\uDEA9.</b></html>");
        instructionLabel.setOpaque(true);
        instructionLabel.setBackground(new Color(223, 255, 248)); // Light cool blue
        instructionLabel.setForeground(new Color(16, 16, 16)); // Dark blue for readability
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(20, 21, 21), 2), // Dark blue border
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
    /**
     * Adds a clickable button for a province on the map.
     * @param province Name of the province.
     * @param x X-coordinate of the button.
     * @param y Y-coordinate of the button.
     */
    private void addProvinceButton(String province, int x, int y) {
        ImageIcon provinceIcon = new ImageIcon("src/main/resources/flag.png");
        JButton provinceButton = new JButton(provinceIcon);
        provinceButton.setBounds(x, y, provinceIcon.getIconWidth(), provinceIcon.getIconHeight());
        provinceButton.setBorderPainted(false);
        provinceButton.setContentAreaFilled(false);
        provinceButton.setFocusPainted(false);

        provinceButton.addActionListener(e -> {
            switch (province) {
                case "Ontario" -> showOntarioAnimals();
                case "Nunavut" -> showNunavutAnimals();
                case "Quebec" -> showQuebecAnimals();
                case "Nova Scotia" -> showNovaScotiaAnimals();
                case "Manitoba" -> showManitobaAnimals();
                case "Saskatchewan" -> showSaskatchewanAnimals();
                case "Alberta" -> showAlbertaAnimals();
                case "British Columbia" -> showBritishColumbiaAnimals();
                case "Yukon" -> showYukonAnimals();
                case "Northwest Territories" -> showNorthwestTerritoriesAnimals();
                case "New Brunswick" -> showNewBrunswickAnimals();
                case "Newfoundland and Labrador" -> showNewfoundlandandLabradorAnimals();
                case "Prince Edward Island" -> showPrinceEdwardIslandAnimals();
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
        ontarioFrame.add(animalsPanel, BorderLayout.CENTER);


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
        quebecFrame.add(animalsPanel, BorderLayout.CENTER);



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
        novascotiaFrame.add(animalsPanel, BorderLayout.CENTER);

        novascotiaFrame.setVisible(true);
    }
    private void showManitobaAnimals() {
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
        ManitobaFrame.add(animalsPanel, BorderLayout.CENTER);


        ManitobaFrame.setVisible(true);

    }
    private void showSaskatchewanAnimals() {
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
        SaskatchewanFrame.add(animalsPanel, BorderLayout.CENTER);



        SaskatchewanFrame.setVisible(true);

    }
    private void showAlbertaAnimals() {
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
        AlbertaFrame.add(animalsPanel, BorderLayout.CENTER);



        AlbertaFrame.setVisible(true);

    }
    private void showBritishColumbiaAnimals() {
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
        BritishColumbiaFrame.add(animalsPanel, BorderLayout.CENTER);

        BritishColumbiaFrame.setVisible(true);

    }
    private void showYukonAnimals() {
        JFrame YukonFrame = new JFrame("Top 5 Native Animals in Yukon");
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
        YukonFrame.add(animalsPanel, BorderLayout.CENTER);

        YukonFrame.setVisible(true);
    }
    private void showNorthwestTerritoriesAnimals() {
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
        NorthwestTerritoriesFrame.add(animalsPanel, BorderLayout.CENTER);

        NorthwestTerritoriesFrame.setVisible(true);
    }
    private void showNewBrunswickAnimals() {
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
        NreBrunswickFrame.add(animalsPanel, BorderLayout.CENTER);

        NreBrunswickFrame.setVisible(true);

    }
    private void showPrinceEdwardIslandAnimals() {
        JFrame PrinceEdwardIslandFrame = new JFrame("Top 5 Native Animals in Prince Edward Island");
        PrinceEdwardIslandFrame.setSize(800, 750);
        PrinceEdwardIslandFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        PrinceEdwardIslandFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {"Northern Shrike", "Island Shrew", "Great Blue Heron", "Eastern Box Turtle", "PEI Jumping Spider"};

        String[] descriptions = {
                "The Northern Shrike is a predatory songbird known for its habit of impaling its prey on thorns or barbed wire. Found across PEI's fields and open woodlands, it hunts small mammals, birds, and insects, and is especially visible during the colder months.",
                "A small, insectivorous mammal, the Island Shrew is native to the woodlands and meadows of Prince Edward Island. Its high metabolism allows it to be active year-round, foraging for insects, worms, and other small creatures.",
                "A large, elegant bird often seen in PEI's coastal wetlands. Known for its long legs and distinctive blue-gray feathers, the Great Blue Heron is a skilled fisherman that feeds on fish, amphibians, and small mammals.",
                "This turtle is known for its distinct, domed shell and its ability to completely close its shell for protection. It is primarily found in the woodlands of PEI and is an important part of the island's biodiversity.",
                "A unique species of jumping spider that thrives in the grasslands and forests of Prince Edward Island. It is known for its excellent vision and acrobatic hunting abilities, often preying on insects much larger than itself."
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
            JLabel animalLabel = new JLabel("<html><b>" + animals[i] + "</b><br>" + descriptions[i] + "</html>");
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add components
            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            final int index = i;
            animalPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PrinceEdwardIslandAnimalDetails(animals[index], descriptions[index], animalImages[index]);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Set the hover effect background color
                    animalPanel.setBackground(new Color(186, 237, 235));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Reset background color when mouse exits
                    animalPanel.setBackground(lightBlue);
                }
            });

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        PrinceEdwardIslandFrame.add(scrollPane, BorderLayout.CENTER);
        PrinceEdwardIslandFrame.add(animalsPanel, BorderLayout.CENTER);

        PrinceEdwardIslandFrame.setVisible(true);
    }

    private void PrinceEdwardIslandAnimalDetails(String name, String description, String thumbnailPath) {
        JFrame detailsFrame = new JFrame(name + " - Detailed Information");
        detailsFrame.setSize(1200, 750);  // Wider to fit gallery on the right
        detailsFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Choose a special "main" image for each animal
        String mainImagePath = switch (name) {
            case "Northern Shrike" -> "src/main/resources/PrinceEdwardIsland/shrike.jpg";
            case "Island Shrew" -> "src/main/resources/PrinceEdwardIsland/shrew.jpg";
            case "Great Blue Heron" -> "src/main/resources/PrinceEdwardIsland/heron.jpg";
            case "Eastern Box Turtle" -> "src/main/resources/PrinceEdwardIsland/turtle.jpg";
            case "PEI Jumping Spider" -> "src/main/resources/PrinceEdwardIsland/spider.png";
            default -> thumbnailPath;  // Fallback to clicked image if no special one exists
        };

        // Main Image + Description Panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        ImageIcon icon = new ImageIcon(mainImagePath);
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel mainImageLabel = new JLabel(new ImageIcon(scaledImage));
        topPanel.add(mainImageLabel, BorderLayout.WEST);

        JTextArea descArea = new JTextArea(description);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(mainPanel.getBackground());
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane descScrollPane = new JScrollPane(descArea);
        descScrollPane.setPreferredSize(new Dimension(400, 200));
        topPanel.add(descScrollPane, BorderLayout.CENTER);

        // Facts Section
        String facts = PrinceEdwardIslandAnimalFacts(name);
        JTextArea factsArea = new JTextArea("\n" + facts);
        factsArea.setLineWrap(true);
        factsArea.setWrapStyleWord(true);
        factsArea.setEditable(false);
        factsArea.setBackground(mainPanel.getBackground());
        factsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane factsScrollPane = new JScrollPane(factsArea);
        factsScrollPane.setBorder(BorderFactory.createTitledBorder("Interesting Facts"));

        // Population Graph
        System.out.print(name);
        JPanel graphPanel = PrinceEdwardIslandPopulationGraph(name);

        // Right-Side Gallery Panel
        JPanel galleryPanel = new JPanel();
        galleryPanel.setLayout(new BoxLayout(galleryPanel, BoxLayout.Y_AXIS));
        galleryPanel.setBorder(BorderFactory.createTitledBorder("Gallery"));
        galleryPanel.setPreferredSize(new Dimension(250, 0));  // Fixed width

        String[] additionalImages = switch (name) {
            case "Northern Shrike" -> new String[]{
                    "src/main/resources/PrinceEdwardIsland/shrike1.jpg",
                    "src/main/resources/PrinceEdwardIsland/shrike2.jpg",
                    "src/main/resources/PrinceEdwardIsland/shrike3.jpg",
                    "src/main/resources/PrinceEdwardIsland/shrike4.jpg"
            };
            case "Island Shrew" -> new String[]{
                    "src/main/resources/PrinceEdwardIsland/shrew1.jpg",
                    "src/main/resources/PrinceEdwardIsland/shrew2.jpg",
                    "src/main/resources/PrinceEdwardIsland/shrew3.jpg",
                    "src/main/resources/PrinceEdwardIsland/shrew4.jpg"
            };
            case "Great Blue Heron" -> new String[]{
                    "src/main/resources/PrinceEdwardIsland/heron1.jpg",
                    "src/main/resources/PrinceEdwardIsland/heron2.jpg",
                    "src/main/resources/PrinceEdwardIsland/heron3.jpg",
                    "src/main/resources/PrinceEdwardIsland/heron4.jpg"
            };
            case "Eastern Box Turtle" -> new String[]{
                    "src/main/resources/PrinceEdwardIsland/turtle1.jpg",
                    "src/main/resources/PrinceEdwardIsland/turtle2.jpg",
                    "src/main/resources/PrinceEdwardIsland/turtle3.jpg",
                    "src/main/resources/PrinceEdwardIsland/turtle4.jpg"
            };
            case "PEI Jumping Spider" -> new String[]{
                    "src/main/resources/PrinceEdwardIsland/spider1.jpg",
                    "src/main/resources/PrinceEdwardIsland/spider2.jpg",
                    "src/main/resources/PrinceEdwardIsland/spider3.jpg",
                    "src/main/resources/PrinceEdwardIsland/spider4.jpg"
            };
            default -> new String[0];
        };

        // Add thumbnails to gallery
        for (String imgPath : additionalImages) {
            ImageIcon thumbIcon = new ImageIcon(imgPath);
            Image thumbImage = thumbIcon.getImage().getScaledInstance(220, 147, Image.SCALE_SMOOTH);
            JLabel thumbLabel = new JLabel(new ImageIcon(thumbImage));
            thumbLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            thumbLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            thumbLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            thumbLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PrinceEdwardIslandFullImage(imgPath);
                }
            });

            galleryPanel.add(Box.createVerticalStrut(10));
            galleryPanel.add(thumbLabel);
        }

        // Layout Assembly
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(factsScrollPane, BorderLayout.CENTER);
        mainPanel.add(graphPanel, BorderLayout.SOUTH);

        JScrollPane mainScrollPane = new JScrollPane(mainPanel);

        detailsFrame.add(mainScrollPane, BorderLayout.CENTER);
        detailsFrame.add(galleryPanel, BorderLayout.EAST);

        detailsFrame.setVisible(true);
    }
    /**
     * Opens a new window to display a full-sized image of the selected animal.
     * @param imgPath The file path to the image to be displayed.
     */
    private void PrinceEdwardIslandFullImage(String imgPath) {
        JFrame imageFrame = new JFrame("Full Image");
        imageFrame.setSize(600, 600);
        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(550, 550, Image.SCALE_SMOOTH)));
        imageFrame.add(imageLabel);
        imageFrame.setVisible(true);
    }
    /**
     * Provides a set of interesting facts about a given Nunavut animal.
     * @param name The name of the animal.
     * @return A string containing facts about the specified animal.
     */
    private String PrinceEdwardIslandAnimalFacts(String name) {
        return switch (name) {
            case "Northern Shrike" -> """
            • Scientific Name: Lanius excubitor
            • The Northern Shrike is a predatory songbird known for its habit of impaling its prey on thorns or barbed wire. Found across PEI's fields and open woodlands, it hunts small mammals, birds, and insects, and is especially visible during the colder months.
        """;
            case "Island Shrew" -> """
            • Scientific Name: Sorex palustris
            • A small, insectivorous mammal, the Island Shrew is native to the woodlands and meadows of Prince Edward Island. Its high metabolism allows it to be active year-round, foraging for insects, worms, and other small creatures.        ""\";
        """;
            case "Great Blue Heron" -> """
            • Scientific Name: Ardea herodias
            • A large, elegant bird often seen in PEI's coastal wetlands. Known for its long legs and distinctive blue-gray feathers, the Great Blue Heron is a skilled fisherman that feeds on fish, amphibians, and small mammals.
        """;
            case "Eastern Box Turtle" -> """
            • Scientific Name: Terrapene carolina
            • This turtle is known for its distinct, domed shell and its ability to completely close its shell for protection. It is primarily found in the woodlands of PEI and is an important part of the island's biodiversity.
        """;
            case "PEI Jumping Spider" -> """
            • Scientific Name: Habronattus oregonensis
            • A unique species of jumping spider that thrives in the grasslands and forests of Prince Edward Island. It is known for its excellent vision and acrobatic hunting abilities, often preying on insects much larger than itself.
        """;
            default -> "No facts available.";
        };
    }

    /**
     * Generates a JPanel containing a population graph for a given Nunavut animal.
     * The graph displays population trends from 1980 to the present.
     * @param animalName The name of the animal.
     * @return JPanel containing the population graph.
     */
    private JPanel PrinceEdwardIslandPopulationGraph(String animalName) {
        int[] years = {1980, 1990, 2000, 2010, 2020, 2025};

        int[] populationData = switch (animalName) {
            case "Northern Shrike" -> new int[]{26000, 25000, 24500, 23000, 22000, 21500};  // sample data
            case "Island Shrew" -> new int[]{180000, 160000, 140000, 120000, 100000, 95000};
            case "Great Blue Heron" -> new int[]{100000, 95000, 90000, 85000, 80000, 78000};
            case "Eastern Box Turtle" -> new int[]{15000, 14500, 14000, 13000, 12500, 12000};
            case "PEI Jumping Spider" -> new int[]{90000, 88000, 86000, 84000, 82000, 81000};
            default -> new int[]{0, 0, 0, 0, 0, 0};
        };

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, width, height);

                // Draw axes
                g2.setColor(Color.BLACK);
                g2.drawLine(50, height - 50, width - 50, height - 50); // X-axis
                g2.drawLine(50, height - 50, 50, 50); // Y-axis

                // Plot data points
                int graphHeight = height - 100;
                int graphWidth = width - 100;
                int xStep = graphWidth / (years.length - 1);

                int maxPopulation = 0;
                for (int pop : populationData) {
                    if (pop > maxPopulation) maxPopulation = pop;
                }

                int prevX = 50, prevY = height - 50 - (populationData[0] * graphHeight / maxPopulation);

                g2.setColor(Color.BLUE);
                for (int i = 0; i < years.length; i++) {
                    int x = 50 + (i * xStep);
                    int y = height - 50 - (populationData[i] * graphHeight / maxPopulation);

                    g2.fillOval(x - 3, y - 3, 6, 6);

                    if (i > 0) {
                        g2.drawLine(prevX, prevY, x, y);
                    }

                    prevX = x;
                    prevY = y;

                    // Add year labels
                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(years[i]), x - 15, height - 30);
                }

                // Y-axis labels (population)
                for (int i = 0; i <= 5; i++) {
                    int yLabel = maxPopulation * i / 5;
                    int yPos = height - 50 - (yLabel * graphHeight / maxPopulation);
                    g2.drawString(yLabel + "", 10, yPos + 5);
                }
            }
        };

        graphPanel.setPreferredSize(new Dimension(600, 200));
        graphPanel.setBorder(BorderFactory.createTitledBorder("Population Growth in Canada (1980-Present)"));

        return graphPanel;
    }












    private void showNewfoundlandandLabradorAnimals() {
        JFrame NewfoundlandFrame = new JFrame("Top 5 Native Animals in Newfoundland and Labrador");
        NewfoundlandFrame.setSize(800, 750);
        NewfoundlandFrame.setLayout(new BorderLayout());

        // Soft blue background
        Color lightBlue = new Color(230, 230, 230);
        NewfoundlandFrame.getContentPane().setBackground(lightBlue);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 10px spacing for a cleaner look
        animalsPanel.setBackground(lightBlue);

        String[] animals = {"Labrador Retriever", "Northern Fur Seal", "Pine Marten", "Carribean Sea Star", "Minke Whale"};

        String[] descriptions = {
                "Famed worldwide, the Labrador Retriever is a breed of dog originating from the region. It was initially bred for retrieving fish and game, known for its intelligence and friendly demeanor.",
                "This species is found in the waters off Newfoundland and Labrador. Northern Fur Seals are known for their thick fur and agility in the water, where they hunt fish and squid.",
                "A small, carnivorous mammal found in the forests of Newfoundland and Labrador. It is elusive, with a dark brown fur coat, and preys on small mammals, birds, and insects.",
                "Found in the coastal waters of Newfoundland and Labrador, the Caribbean Sea Star is a brightly colored starfish. While its name suggests warmer waters, it's found in colder regions during specific migratory patterns.",
                "One of the smaller baleen whales, the Minke Whale can be spotted off the coast of Newfoundland and Labrador. It is known for its distinctive black-and-white coloration and is often seen feeding in the region's rich waters."
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
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Set the background color for each animal panel explicitly
            animalPanel.setBackground(lightBlue);

            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

            JLabel animalLabel = new JLabel("<html><b>" + animals[i] + "</b><br>" + descriptions[i] + "</html>");
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            final int index = i;
            animalPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    NewfoundlandandLabradorAnimalDetails(animals[index], descriptions[index], animalImages[index]);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Set the hover effect background color
                    animalPanel.setBackground(new Color(186, 237, 235));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Reset background color when mouse exits
                    animalPanel.setBackground(lightBlue);
                }
            });

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        NewfoundlandFrame.add(scrollPane, BorderLayout.CENTER);
        NewfoundlandFrame.add(animalsPanel, BorderLayout.CENTER);

        NewfoundlandFrame.setLocationRelativeTo(null);
        NewfoundlandFrame.setVisible(true);
    }

    private void NewfoundlandandLabradorAnimalDetails(String name, String description, String thumbnailPath) {
        JFrame detailsFrame = new JFrame(name + " - Detailed Information");
        detailsFrame.setSize(1200, 750);  // Wider to fit gallery on the right
        detailsFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Choose a special "main" image for each animal
        String mainImagePath = switch (name) {
            case "Dog" -> "src/main/resources/NewfoundlandandLabrador/dog.jpg";
            case "Marten" -> "src/main/resources/NewfoundlandandLabrador/marten.jpg";
            case "Seal" -> "src/main/resources/NewfoundlandandLabrador/seal.jpg";
            case "Star" -> "src/main/resources/NewfoundlandandLabrador/star.jpg";
            case "Whale" -> "src/main/resources/NewfoundlandandLabrador/whale.png";
            default -> thumbnailPath;  // Fallback to clicked image if no special one exists
        };

        // Main Image + Description Panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        ImageIcon icon = new ImageIcon(mainImagePath);
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel mainImageLabel = new JLabel(new ImageIcon(scaledImage));
        topPanel.add(mainImageLabel, BorderLayout.WEST);

        JTextArea descArea = new JTextArea(description);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(mainPanel.getBackground());
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane descScrollPane = new JScrollPane(descArea);
        descScrollPane.setPreferredSize(new Dimension(400, 200));
        topPanel.add(descScrollPane, BorderLayout.CENTER);

        // Facts Section
        String facts = NewfoundlandandLabradorAnimalFacts(name);
        JTextArea factsArea = new JTextArea("\n" + facts);
        factsArea.setLineWrap(true);
        factsArea.setWrapStyleWord(true);
        factsArea.setEditable(false);
        factsArea.setBackground(mainPanel.getBackground());
        factsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane factsScrollPane = new JScrollPane(factsArea);
        factsScrollPane.setBorder(BorderFactory.createTitledBorder("Interesting Facts"));

        // Population Graph
        JPanel graphPanel = NewfoundlandandLabradorPopulationGraph(name);

        // Right-Side Gallery Panel
        JPanel galleryPanel = new JPanel();
        galleryPanel.setLayout(new BoxLayout(galleryPanel, BoxLayout.Y_AXIS));
        galleryPanel.setBorder(BorderFactory.createTitledBorder("Gallery"));
        galleryPanel.setPreferredSize(new Dimension(250, 0));  // Fixed width

        String[] additionalImages = switch (name) {
            case "Labrador Retriever" -> new String[]{
                    "src/main/resources/NewfoundlandandLabrador/dog1.jpg",
                    "src/main/resources/NewfoundlandandLabrador/dog2.png",
                    "src/main/resources/NewfoundlandandLabrador/dog3.jpg",
                    "src/main/resources/NewfoundlandandLabrador/dog4.jpg"
            };
            case "Pine Marten" -> new String[]{
                    "src/main/resources/NewfoundlandandLabrador/marten1.jpg",
                    "src/main/resources/NewfoundlandandLabrador/marten2.jpg",
                    "src/main/resources/NewfoundlandandLabrador/marten3.jpg",
                    "src/main/resources/NewfoundlandandLabrador/marten4.jpg"
            };
            case "Northern Fur Seal" -> new String[]{
                    "src/main/resources/NewfoundlandandLabrador/seal1.jpg",
                    "src/main/resources/NewfoundlandandLabrador/seal2.jpg",
                    "src/main/resources/NewfoundlandandLabrador/seal3.jpg",
                    "src/main/resources/NewfoundlandandLabrador/seal4.jpg"
            };
            case "Carribean Sea Star" -> new String[]{
                    "src/main/resources/NewfoundlandandLabrador/star1.jpg",
                    "src/main/resources/NewfoundlandandLabrador/star2.jpg",
                    "src/main/resources/NewfoundlandandLabrador/star3.jpg",
                    "src/main/resources/NewfoundlandandLabrador/star4.jpg"
            };
            case "Minke Whale" -> new String[]{
                    "src/main/resources/NewfoundlandandLabrador/whale1.jpg",
                    "src/main/resources/NewfoundlandandLabrador/whale2.jpg",
                    "src/main/resources/NewfoundlandandLabrador/whale3.jpg",
                    "src/main/resources/NewfoundlandandLabrador/whale4.jpg"
            };
            default -> new String[0];
        };

        // Add thumbnails to gallery
        for (String imgPath : additionalImages) {
            ImageIcon thumbIcon = new ImageIcon(imgPath);
            Image thumbImage = thumbIcon.getImage().getScaledInstance(220, 147, Image.SCALE_SMOOTH);
            JLabel thumbLabel = new JLabel(new ImageIcon(thumbImage));
            thumbLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            thumbLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            thumbLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            thumbLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    NewfoundlandandLabradorFullImage(imgPath);
                }
            });

            galleryPanel.add(Box.createVerticalStrut(10));
            galleryPanel.add(thumbLabel);
        }

        // Layout Assembly
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(factsScrollPane, BorderLayout.CENTER);
        mainPanel.add(graphPanel, BorderLayout.SOUTH);

        JScrollPane mainScrollPane = new JScrollPane(mainPanel);

        detailsFrame.add(mainScrollPane, BorderLayout.CENTER);
        detailsFrame.add(galleryPanel, BorderLayout.EAST);

        detailsFrame.setVisible(true);
    }
    /**
     * Opens a new window to display a full-sized image of the selected animal.
     * @param imgPath The file path to the image to be displayed.
     */
    private void NewfoundlandandLabradorFullImage(String imgPath) {
        JFrame imageFrame = new JFrame("Full Image");
        imageFrame.setSize(600, 600);
        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(550, 550, Image.SCALE_SMOOTH)));
        imageFrame.add(imageLabel);
        imageFrame.setVisible(true);
    }
    /**
     * Provides a set of interesting facts about a given Nunavut animal.
     * @param name The name of the animal.
     * @return A string containing facts about the specified animal.
     */
    private String NewfoundlandandLabradorAnimalFacts(String name) {
        return switch (name) {
            case "Labrador Retriever" -> """
            • Scientific Name: Canis Lupus Familiaris
            • Famed worldwide, the Labrador Retriever is a breed of dog originating from the region. It was initially bred for retrieving fish and game, known for its intelligence and friendly demeanor.
        """;
            case "Northern Fur Seal" -> """
            • Scientific Name: Callorhinus ursinus
            • This species is found in the waters off Newfoundland and Labrador. Northern Fur Seals are known for their thick fur and agility in the water, where they hunt fish and squid.
        """;
            case "Pine Marten" -> """
            • Scientific Name: Martes martes
            • A small, carnivorous mammal found in the forests of Newfoundland and Labrador. It is elusive, with a dark brown fur coat, and preys on small mammals, birds, and insects.
        """;
            case "Caribbean Sea Star" -> """
            • Scientific Name: Oreaster reticulatus
            • Found in the coastal waters of Newfoundland and Labrador, the Caribbean Sea Star is a brightly colored starfish. While its name suggests warmer waters, it's found in colder regions during specific migratory patterns.
        """;
            case "Minke Whale" -> """
            • Scientific Name: Delphinapterus leucas
            • One of the smaller baleen whales, the Minke Whale can be spotted off the coast of Newfoundland and Labrador. It is known for its distinctive black-and-white coloration and is often seen feeding in the region's rich waters.
        """;
            default -> "No facts available.";
        };
    }

    /**
     * Generates a JPanel containing a population graph for a given Nunavut animal.
     * The graph displays population trends from 1980 to the present.
     * @param animalName The name of the animal.
     * @return JPanel containing the population graph.
     */
    private JPanel NewfoundlandandLabradorPopulationGraph(String animalName) {
        int[] years = {1980, 1990, 2000, 2010, 2020, 2025};

        int[] populationData = switch (animalName) {
            case "Labrador Retriever" -> new int[]{26000, 25000, 24500, 23000, 22000, 21500};  // sample data
            case "Northern Fur Seal" -> new int[]{180000, 160000, 140000, 120000, 100000, 95000};
            case "Pine Marten" -> new int[]{100000, 95000, 90000, 85000, 80000, 78000};
            case "Carribean Sea Star" -> new int[]{15000, 14500, 14000, 13000, 12500, 12000};
            case "Minke Whale" -> new int[]{90000, 88000, 86000, 84000, 82000, 81000};
            default -> new int[]{0, 0, 0, 0, 0, 0};
        };

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, width, height);

                // Draw axes
                g2.setColor(Color.BLACK);
                g2.drawLine(50, height - 50, width - 50, height - 50); // X-axis
                g2.drawLine(50, height - 50, 50, 50); // Y-axis

                // Plot data points
                int graphHeight = height - 100;
                int graphWidth = width - 100;
                int xStep = graphWidth / (years.length - 1);

                int maxPopulation = 0;
                for (int pop : populationData) {
                    if (pop > maxPopulation) maxPopulation = pop;
                }

                int prevX = 50, prevY = height - 50 - (populationData[0] * graphHeight / maxPopulation);

                g2.setColor(Color.BLUE);
                for (int i = 0; i < years.length; i++) {
                    int x = 50 + (i * xStep);
                    int y = height - 50 - (populationData[i] * graphHeight / maxPopulation);

                    g2.fillOval(x - 3, y - 3, 6, 6);

                    if (i > 0) {
                        g2.drawLine(prevX, prevY, x, y);
                    }

                    prevX = x;
                    prevY = y;

                    // Add year labels
                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(years[i]), x - 15, height - 30);
                }

                // Y-axis labels (population)
                for (int i = 0; i <= 5; i++) {
                    int yLabel = maxPopulation * i / 5;
                    int yPos = height - 50 - (yLabel * graphHeight / maxPopulation);
                    g2.drawString(yLabel + "", 10, yPos + 5);
                }
            }
        };

        graphPanel.setPreferredSize(new Dimension(600, 200));
        graphPanel.setBorder(BorderFactory.createTitledBorder("Population Growth in Canada (1980-Present)"));

        return graphPanel;
    }























    /**
     * Displays a window showcasing the top 5 native animals in Nunavut.
     * Each animal is shown with an image, a short description, and a hover effect.
     * Clicking on an animal opens a detailed information window.
     */
    public JFrame showNunavutAnimals() {
        JFrame nunavutFrame = new JFrame("Top 5 Native Animals in Nunavut");
        nunavutFrame.setSize(800, 750);
        nunavutFrame.setLayout(new BorderLayout());

        Color lightBlue = new Color(230, 230, 230);

        JPanel animalsPanel = new JPanel();
        animalsPanel.setLayout(new GridLayout(5, 1, 10, 10));
        animalsPanel.setBackground(lightBlue);  // Set background color for the animals panel

        String[] animals = {"Polar Bear", "Caribou", "Arctic Fox", "Snowy Owl", "Beluga Whale"};
        String[] descriptions = {
                "Known for its white fur and strong build, the polar bear is an iconic species of the Arctic. They are found primarily on the sea ice and are known for being skilled hunters, primarily preying on seals.",
                "A large herbivorous mammal, the caribou is well-adapted to the harsh Arctic conditions. They are often seen in large herds and play a key role in the local ecosystem.",
                "The Arctic fox is a small mammal with a thick coat that changes color with the seasons, from white in winter to brown in summer. It survives the cold winters of Nunavut by hunting small mammals and scavenging.",
                "The snowy owl is a large, white owl that is found in the tundra regions. It has excellent hunting skills, preying on lemmings and other small animals.",
                "These small white whales are found in the Arctic and sub-Arctic waters. They are highly social creatures, often seen in pods, and are well adapted to the cold waters."
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
            animalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Set the background color for each animal panel explicitly
            animalPanel.setBackground(lightBlue);

            ImageIcon icon = new ImageIcon(animalImages[i]);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

            JLabel animalLabel = new JLabel("<html><b>" + animals[i] + "</b><br>" + descriptions[i] + "</html>");
            animalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            animalPanel.add(imageLabel, BorderLayout.WEST);
            animalPanel.add(animalLabel, BorderLayout.CENTER);

            final int index = i;
            animalPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    NunavutAnimalDetails(animals[index], descriptions[index], animalImages[index]);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Set the hover effect background color
                    animalPanel.setBackground(new Color(186, 237, 235));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Reset background color when mouse exits
                    animalPanel.setBackground(lightBlue);
                }
            });

            animalsPanel.add(animalPanel);
        }

        JScrollPane scrollPane = new JScrollPane(animalsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        nunavutFrame.add(scrollPane, BorderLayout.CENTER);
        nunavutFrame.add(animalsPanel, BorderLayout.CENTER);

        nunavutFrame.setLocationRelativeTo(null);
        nunavutFrame.setVisible(true);

        return nunavutFrame;
    }
    /**
     * Opens a detailed information window for a selected Nunavut animal.
     * The window includes a main image, description, interesting facts, population graph, and gallery images.
     * @param name The name of the animal.
     * @param description A brief description of the animal.
     * @param thumbnailPath Path to the thumbnail image displayed in the list view.
     */
    private void NunavutAnimalDetails(String name, String description, String thumbnailPath) {
        JFrame detailsFrame = new JFrame(name + " - Detailed Information");
        detailsFrame.setSize(1200, 750);  // Wider to fit gallery on the right
        detailsFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Choose a special "main" image for each animal
        String mainImagePath = switch (name) {
            case "Polar Bear" -> "src/main/resources/Nunavut/bearmain.jpeg";
            case "Caribou" -> "src/main/resources/Nunavut/cariboumain.jpg";
            case "Arctic Fox" -> "src/main/resources/Nunavut/foxmain.jpg";
            case "Snowy Owl" -> "src/main/resources/Nunavut/owlmain.jpg";
            case "Beluga Whale" -> "src/main/resources/Nunavut/whalemain.png";
            default -> thumbnailPath;  // Fallback to clicked image if no special one exists
        };

        // Main Image + Description Panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        ImageIcon icon = new ImageIcon(mainImagePath);
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel mainImageLabel = new JLabel(new ImageIcon(scaledImage));
        topPanel.add(mainImageLabel, BorderLayout.WEST);

        JTextArea descArea = new JTextArea(description);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(mainPanel.getBackground());
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane descScrollPane = new JScrollPane(descArea);
        descScrollPane.setPreferredSize(new Dimension(400, 200));
        topPanel.add(descScrollPane, BorderLayout.CENTER);

        // Facts Section
        String facts = NunavutAnimalFacts(name);
        JTextArea factsArea = new JTextArea("\n" + facts);
        factsArea.setLineWrap(true);
        factsArea.setWrapStyleWord(true);
        factsArea.setEditable(false);
        factsArea.setBackground(mainPanel.getBackground());
        factsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane factsScrollPane = new JScrollPane(factsArea);
        factsScrollPane.setBorder(BorderFactory.createTitledBorder("Interesting Facts"));

        // Population Graph
        JPanel graphPanel = NunavutPopulationGraph(name);

        // Right-Side Gallery Panel
        JPanel galleryPanel = new JPanel();
        galleryPanel.setLayout(new BoxLayout(galleryPanel, BoxLayout.Y_AXIS));
        galleryPanel.setBorder(BorderFactory.createTitledBorder("Gallery"));
        galleryPanel.setPreferredSize(new Dimension(250, 0));  // Fixed width

        String[] additionalImages = switch (name) {
            case "Polar Bear" -> new String[]{
                    "src/main/resources/Nunavut/bear1.jpg",
                    "src/main/resources/Nunavut/bear2.png",
                    "src/main/resources/Nunavut/bear3.jpg",
                    "src/main/resources/Nunavut/bear4.jpg"
            };
            case "Caribou" -> new String[]{
                    "src/main/resources/Nunavut/caribou1.jpg",
                    "src/main/resources/Nunavut/caribou2.jpg",
                    "src/main/resources/Nunavut/caribou3.jpg",
                    "src/main/resources/Nunavut/caribou4.jpg"
            };
            case "Arctic Fox" -> new String[]{
                    "src/main/resources/Nunavut/fox1.jpg",
                    "src/main/resources/Nunavut/fox2.jpg",
                    "src/main/resources/Nunavut/fox3.jpg",
                    "src/main/resources/Nunavut/fox4.jpg"
            };
            case "Snowy Owl" -> new String[]{
                    "src/main/resources/Nunavut/owl1.jpg",
                    "src/main/resources/Nunavut/owl2.jpg",
                    "src/main/resources/Nunavut/owl3.jpg",
                    "src/main/resources/Nunavut/owl4.jpg"
            };
            case "Beluga Whale" -> new String[]{
                    "src/main/resources/Nunavut/whale1.jpg",
                    "src/main/resources/Nunavut/whale2.jpeg",
                    "src/main/resources/Nunavut/whale3.jpg",
                    "src/main/resources/Nunavut/whale4.jpg"
            };
            default -> new String[0];
        };

        // Add thumbnails to gallery
        for (String imgPath : additionalImages) {
            ImageIcon thumbIcon = new ImageIcon(imgPath);
            Image thumbImage = thumbIcon.getImage().getScaledInstance(220, 147, Image.SCALE_SMOOTH);
            JLabel thumbLabel = new JLabel(new ImageIcon(thumbImage));
            thumbLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            thumbLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            thumbLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            thumbLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    NunavutFullImage(imgPath);
                }
            });

            galleryPanel.add(Box.createVerticalStrut(10));
            galleryPanel.add(thumbLabel);
        }

        // Layout Assembly
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(factsScrollPane, BorderLayout.CENTER);
        mainPanel.add(graphPanel, BorderLayout.SOUTH);

        JScrollPane mainScrollPane = new JScrollPane(mainPanel);

        detailsFrame.add(mainScrollPane, BorderLayout.CENTER);
        detailsFrame.add(galleryPanel, BorderLayout.EAST);

        detailsFrame.setVisible(true);
    }
    /**
     * Opens a new window to display a full-sized image of the selected animal.
     * @param imgPath The file path to the image to be displayed.
     */
    private void NunavutFullImage(String imgPath) {
        JFrame imageFrame = new JFrame("Full Image");
        imageFrame.setSize(600, 600);
        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(550, 550, Image.SCALE_SMOOTH)));
        imageFrame.add(imageLabel);
        imageFrame.setVisible(true);
    }
    /**
     * Provides a set of interesting facts about a given Nunavut animal.
     * @param name The name of the animal.
     * @return A string containing facts about the specified animal.
     */
    private String NunavutAnimalFacts(String name) {
        return switch (name) {
            case "Polar Bear" -> """
            • Scientific Name: Ursus maritimus
            • Polar bears are excellent swimmers, covering long distances between ice sheets.
            • Their fur appears white but is actually translucent.
            • They rely heavily on sea ice to hunt seals.
        """;
            case "Caribou" -> """
            • Scientific Name: Rangifer tarandus
            • Caribou undertake one of the longest migrations of any land mammal.
            • Both males and females grow antlers.
            • They are critical to Inuit culture and subsistence.
        """;
            case "Arctic Fox" -> """
            • Scientific Name: Vulpes lagopus
            • Their fur changes from white in winter to brown in summer for camouflage.
            • They can survive temperatures as low as -50°C.
            • They often follow polar bears to scavenge leftover prey.
        """;
            case "Snowy Owl" -> """
            • Scientific Name: Bubo scandiacus
            • Unlike many owls, snowy owls are active during the day.
            • They have excellent vision and hearing for hunting lemmings.
            • Females are larger and have more black spots than males.
        """;
            case "Beluga Whale" -> """
            • Scientific Name: Delphinapterus leucas
            • Belugas are highly vocal and communicate with clicks and whistles.
            • They can swim backwards!
            • They shed their outer skin every summer.
        """;
            default -> "No facts available.";
        };
    }
    /**
     * Generates a JPanel containing a population graph for a given Nunavut animal.
     * The graph displays population trends from 1980 to the present.
     * @param animalName The name of the animal.
     * @return JPanel containing the population graph.
     */
    private JPanel NunavutPopulationGraph(String animalName) {
        int[] years = {1980, 1990, 2000, 2010, 2020, 2025};

        int[] populationData = switch (animalName) {
            case "Polar Bear" -> new int[]{26000, 25000, 24500, 23000, 22000, 21500};  // sample data
            case "Caribou" -> new int[]{180000, 160000, 140000, 120000, 100000, 95000};
            case "Arctic Fox" -> new int[]{100000, 95000, 90000, 85000, 80000, 78000};
            case "Snowy Owl" -> new int[]{15000, 14500, 14000, 13000, 12500, 12000};
            case "Beluga Whale" -> new int[]{90000, 88000, 86000, 84000, 82000, 81000};
            default -> new int[]{0, 0, 0, 0, 0, 0};
        };

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, width, height);

                // Draw axes
                g2.setColor(Color.BLACK);
                g2.drawLine(50, height - 50, width - 50, height - 50); // X-axis
                g2.drawLine(50, height - 50, 50, 50); // Y-axis

                // Plot data points
                int graphHeight = height - 100;
                int graphWidth = width - 100;
                int xStep = graphWidth / (years.length - 1);

                int maxPopulation = 0;
                for (int pop : populationData) {
                    if (pop > maxPopulation) maxPopulation = pop;
                }

                int prevX = 50, prevY = height - 50 - (populationData[0] * graphHeight / maxPopulation);

                g2.setColor(Color.BLUE);
                for (int i = 0; i < years.length; i++) {
                    int x = 50 + (i * xStep);
                    int y = height - 50 - (populationData[i] * graphHeight / maxPopulation);

                    g2.fillOval(x - 3, y - 3, 6, 6);

                    if (i > 0) {
                        g2.drawLine(prevX, prevY, x, y);
                    }

                    prevX = x;
                    prevY = y;

                    // Add year labels
                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(years[i]), x - 15, height - 30);
                }

                // Y-axis labels (population)
                for (int i = 0; i <= 5; i++) {
                    int yLabel = maxPopulation * i / 5;
                    int yPos = height - 50 - (yLabel * graphHeight / maxPopulation);
                    g2.drawString(yLabel + "", 10, yPos + 5);
                }
            }
        };

        graphPanel.setPreferredSize(new Dimension(600, 200));
        graphPanel.setBorder(BorderFactory.createTitledBorder("Population Growth in Canada (1980-Present)"));

        return graphPanel;
    }

    private static void initializeUserDataFile() {
        try {
            File userFile = new File(USER_DATA_FILE);

            // Create parent directories if they don't exist
            userFile.getParentFile().mkdirs();

            // Check if file exists
            if (!userFile.exists()) {
                // Create new file with headers
                try (FileWriter writer = new FileWriter(userFile)) {
                    writer.write("username,password\n");
                }
            } else {
                // Verify if headers exist without overwriting
                boolean hasHeaders = false;
                try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                    String firstLine = reader.readLine();
                    hasHeaders = firstLine != null && firstLine.equals("username,password");
                }

                // Add headers if missing (without losing data)
                if (!hasHeaders) {
                    // Read existing content
                    List<String> lines = Files.readAllLines(userFile.toPath());

                    // Write back with headers
                    try (FileWriter writer = new FileWriter(userFile)) {
                        writer.write("username,password\n");
                        for (String line : lines) {
                            writer.write(line + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error initializing user database:\n" + e.getMessage(),
                    "Initialization Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }



    public static void main(String[] args) {
        try{
            java.nio.file.Path path = java.nio.file.Paths.get(USER_DATA_FILE);
            if(!java.nio.file.Files.exists(path)){
                java.nio.file.Files.createFile(path);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        initializeUserDataFile();

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(); // Create an instance of MainFrame
            mainFrame.setVisible(true); // Show the main frame
        });
    }
}
