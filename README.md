# PROJECT INFORMATION

The following product allows you to see a map of Canada, and click on each province in order to see the most popular native animals. Doing so provides access to information about each animal including their location, population, diet, status of conversation as well as any fun facts related. This is to keep track of wildlife populations and relevant information in order to aid in wildlife preservation and protection of natural resources.

# PRODUCT INSTRUCTIONS

1. Clone the product into your own device using terminal command "git clone"
2. Open the folder using preferred coding software and run the "MainFrame.java" file.
3. Once the UI pops up, you are prompted to either make a login or continue as a guest user. 
4. From there, you can enjoy all features:
   - Click on "Subscribe to Newsletter" to be updated on all animal related information
   - Click on "View Articles" to see any relevant articles on animals, species, endangerment, etc.
   -  Click on "Endangered List" to see a list of all animals residing in Canada that are currently endangered or at risk.
   -  Click on "Not for Hunt" to see a list of all animals residing in Canada that have been legally protected by the Government.
   -  Click on "Some facts" in order to see fun facts about animals.
   -  Click on "Emergency Contact" if you have information you would like to report about the wildlife.

# HOW TO RUN
You are able to run our program by compiling with Maven and Java if you would like to build the jar file. Or you can just run it with java.

## With Mavan and Java 23
Requirements: 
- Java Development Kit (JDK): Version 23 or later: https://www.oracle.com/ca-en/java/technologies/downloads/ 
- Apache Maven: Version 3.8 or later: https://maven.apache.org/download.cgi 
- An IDE (e.g., IntelliJ IDEA, Eclipse) or a terminal for command-line execution

1. Clone the git repository.
   ```bash
    git clone https://github.com/dianaRiyahi/CGHPM.git
    ```
2. Go into the project directory.
   ```bash
    cd CGHPM
   ```
3. Build the Jar File
   ```bash
    mvn clean install
   ```
4. Execute the Jar file with the following command
    ```bash
    mvn exec:java -Dexec.mainClass="CGHPM.MainFrame"
    ```

## With Just Java 23 <a name="J"></a>
Requirements: 
- Java 23: https://www.oracle.com/ca-en/java/technologies/downloads/
  
1. First clone the git repository
    ```bash
    git clone https://github.com/dianaRiyahi/CGHPM.git
    ```
2. Then navigate to the main file with the following command.
    ```bash
    cd CGHPM/src/main/java/MainFrame
    ```
3.  Then finally to run the program run and then the program should pop up.
    ```bash
    java MainFrame.java
    ```

