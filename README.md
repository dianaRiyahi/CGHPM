# Final Course Project - Draw & Guess

This project was developed as a collaborative group assignment for CSCI 2020U: Software Systems Development and Integration. The game draws inspiration from *Skribbl.io* and incorporates a pirate theme to provide an engaging multiplayer drawing and guessing experience.

## Team Members
- Kamand Taghavi  
- Diana Riyahi  
- Zain Syed  
- Shevan Wijeratne  
- Gbemisola Olaseha

---

## Project Overview

**Draw & Guess** is a multiplayer Java-based game where players join lobbies and take turns drawing randomly assigned words while others guess. The game includes pirate-themed characters, background music, sound effects, and scoring logic based on guess speed. The application also demonstrates real-time chat, synchronized drawing, and a turn-based game loop.

---

## Key Features

- Multiplayer lobby system with real-time synchronization
- Drawing canvas with brush, color picker, eraser, and clear screen
- Real-time chat for in-game guessing and communication
- Customizable settings: draw time, number of rounds, and hint frequency
- Scoring system based on response time with leaderboard
- Pirate character selection (unique avatars per player)
- Custom-themed GUI using Java Swing
- Background music and sound effects integration

---

## Technologies & Concepts

### Java Swing
- GUI elements for game flow, drawing panel, buttons, and menus
- Event-driven UI handling for mouse interactions

### Networking
- Client-server architecture using Java sockets
- Lobby creation and communication managed via `GameServer` and `SkribblClientApp`

### Object-Oriented Design
- Separation of concerns: UI (SkribblCloneUI), game logic (Gameplay), and networking (GameServer, GameLobbyClient)
- Encapsulation of player avatars, game state, and drawing tools

### File & Resource Management
- Loads fonts, images, icons, and audio clips from `/main/Resources`
- Assets include background music, sound effects, UI images, and pirate icons

---

## Project Structure

```
main/java/
├── org.example.client
│   ├── SkribblClientApp.java       # Client-server communication
│   ├── SkribblCloneUI.java         # Main GUI and user interaction
│   ├── Gameplay.java               # Game logic, timing, and state tracking
│   └── GameLobbyClient.java       # Lobby data and connection
└── org.example.server
    └── GameServer.java            # Manages all game rooms and interactions
```

---

## Running the Game

### Prerequisites

- Java JDK 8 or later
- Java Swing GUI support (included in standard JDK)
- Game resources placed in: `main/Resources`

### Installation

```bash
git clone https://github.com/OntarioTech-CS-program/w25-csci2020u-finalproject-w25-team23
cd w25-csci2020u-finalproject-w25-team23
```

### Launch Server

```bash
cd main
javac org/example/server/GameServer.java
java org.example.server.GameServer
```

### Launch Client

```bash
cd main
javac org/example/client/SkribblClientApp.java
java org.example.client.SkribblClientApp
```

---

## Gameplay Walkthrough

1. Launch the game and arrive at the welcome screen
2. Select a pirate avatar and enter a username
3. Choose to create a new lobby or join an existing one
4. Wait for all players to join; game starts when ready
5. Players take turns drawing a word while others guess via chat
6. Hints appear gradually during the round
7. Scoring is based on how fast a player correctly guesses
8. Final scores are displayed at the end of all rounds

---

## Resource Files

Ensure the following resources are stored in the `/main/Resources` directory:

### Images
- `MAINUI.png`, `JOINLOBBY.png`, `LOBBYCREATE.png`, `DRAWING.png`, `INSTRUCTIONS.png`, `CHARACTER.png`, `NAME.png`

### Pirate Avatars
- `anne.png`, `calico.png`, `gerald.png`, `henry.png`, `long_john.png`, `mary.png`, `samuel.png`, `stede.png`, `william.png`

### Icons
- `icon23.png`, `icon24.png`

### Fonts
- `TheRumIsGone-Wy1nG.ttf`

### Audio
- `main_menu.wav`, `click.wav`, `drawing_screen.wav`

---

## Screenshots

### Welcome Screen
![Welcome](MAINUI.png)

### Join Lobby
![Join Lobby](JOINLOBBY.png)

### Create Lobby
![Create Lobby](LOBBYCREATION.png)

### Drawing Board
![Drawing Board](DRAWINGBOARD.png)

### Instructions
![Instructions](HOWTOPLAY.png)

### Character Select
![Characters](CHARACTERSELECT.png)

---

## Known Issues

- Minor synchronization delay under high-latency connections
- No profanity filter implemented in chat
- Does not support mobile browsers or screen scaling

---

## Libraries & References

- Java Swing & AWT
- javax.sound.sampled (Audio system)
- Resource loading via `ClassLoader`
- GitHub for version control
- Custom font & sound licensing: Assets for educational use only

---

## Acknowledgments

This game was built as an academic project to apply knowledge in GUI development, socket programming, and software integration. All visual/audio assets are used for educational purposes.