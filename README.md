# Smart Traffic Simulation

A sophisticated, multi-agent urban traffic simulation engine built with JavaFX. This project provides a robust framework for modeling complex traffic interactions, driver behaviors, and urban infrastructure management.

<img width="1280" height="800" alt="fig_image_mode" src="https://github.com/user-attachments/assets/da689f15-4a80-43f6-af7d-5ee96514b65b" />

> [!NOTE]
> The current implementation features a "Dual-Switch" architecture for vehicle rendering. Users can toggle between **Rectangle Mode** (schematic view with ID labels) and **Image Mode** (sprite-based view) using separate dedicated ToggleButtons to ensure clear visual choice.

## Core Capabilities

### Advanced Traffic Engineering
*   **Conflict Resolution Engine**: Implements a hierarchical priority system to resolve intersection conflicts, preventing deadlocks and optimizing throughput.
*   **Coordinate Continuity**: A signed-distance physics system ensures seamless vehicle movement across segment boundaries without visual stutters.
*   **Dynamic Signal Coordination**: Real-time traffic light synchronization integrated with path-finding logic.

### Behavioral Simulation
*   **Heterogeneous Driver AI**: Drivers exhibit distinct behaviors (Normal, Aggressive, Emergency) that adapt to local traffic conditions.
*   **Priority-Based Yielding**: Emergency vehicles (Ambulances, FireTrucks) receive absolute priority, with other vehicles performing reactive yielding.
*   **Smooth Motion Control**: Realistic braking and acceleration curves based on leader-follower distance and signal states.

### Map Infrastructure
*   **Real-time Editor**: Dynamic modification of nodes and roads within the running simulation.
*   **Traffic Regulation**: Granular control over vehicle generation rates and global spawn toggles.
*   **State Management**: Support for complete map resets and default template restoration.

## Technical Architecture

> [!TIP]
> The simulation utilizes a decoupled Handler-Renderer architecture, allowing for independent updates to physics logic and visual representation without affecting performance.

*   **Logic Core**: Java 25 based event-loop simulation.
*   **UI Layer**: JavaFX with CSS-styled components and hardware-accelerated rendering.
*   **Data Management**: Maven-based dependency and lifecycle management.

## Installation

### Prerequisites
*   Java Development Kit (JDK) 25 or higher
*   Apache Maven

### Running the Project
1.  Clone the repository.
2.  Execute the following command in the project root:
    ```bash
    mvn javafx:run
    ```

## Authors
- Phuc, Phung Minh
- Truong, Tran Xuan
- Van, Nguyen Duc
- Phi, Duong Tuan
- Nghia, Le Trong
- Trung, Tran Van
