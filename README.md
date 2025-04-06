# Canadian Wildlife Portal

This project is an interactive Java application designed to promote awareness, education, and conservation of Canadian wildlife. It presents a clickable map of Canada where users can explore native animal species by province. The application provides detailed insights into species characteristics, conservation statuses, and interesting facts.

**Note:** As of the current implementation, detailed animal data is only functional for the *Nunavut* region.

---

## Features

- Interactive map of Canada where users can explore wildlife by province
- Species data includes:
  - Name and habitat
  - Estimated population
  - Dietary habits
  - Conservation status
  - Fun facts and trivia
- Conservation and hunting status breakdown
- Built-in newsletter subscription form
- Curated articles from national wildlife and conservation organizations
- Emergency contact panel for wildlife-related issues

---

## Getting Started

### Option 1: Using Maven and Java 23

**Requirements:**
- Java Development Kit (JDK) 23 or later: [Download JDK](https://www.oracle.com/ca-en/java/technologies/downloads/)
- Apache Maven 3.8 or later: [Download Maven](https://maven.apache.org/download.cgi)
- IDE (IntelliJ IDEA, VS Code) or terminal

**Steps:**

1. Clone the repository
```bash
git clone https://github.com/dianaRiyahi/CGHPM.git
cd CGHPM
```

2. Build the project
```bash
mvn clean install
```

3. Run the application
```bash
mvn exec:java -Dexec.mainClass="CGHPM.MainFrame"
```

---

### Option 2: Running Directly with Java

**Requirements:**
- Java 23

**Steps:**

1. Clone the repository
```bash
git clone https://github.com/dianaRiyahi/CGHPM.git
```

2. Navigate to the source directory
```bash
cd CGHPM/src/main/java/MainFrame
```

3. Run the application
```bash
java MainFrame.java
```

---

## User Guide

1. Launch the application. You will be prompted to log in or continue as a guest.
2. Main menu options:
   - **View Map**: Explore the interactive map of Canada
   - **Subscribe to Newsletter**: Receive wildlife updates via email
   - **View Articles**: Access curated news and information about wildlife
   - **Endangered List**: View a categorized list of endangered animals
   - **Not for Hunt**: List of protected species under Canadian law
   - **Some Facts**: Fun animal facts and trivia
   - **Emergency Contact**: Report incidents or wildlife concerns

---

## Project Limitations

- Currently, detailed animal data and interactions are only implemented for **Nunavut**.
- Other provinces may show placeholder behavior or no data until extended.

---

## Contributors

Developed by:

- Kamand Taghavi  
- Diana Riyahi  
- Zain Syed  
- Shevan Wijeratne  
- Gbemisola Olaseha