# 🎀 EwhaRibbon - Desktop App for Exploring and Reviewing Zoned Restaurants Near Campus

## 📖 Overview
EwhaRibbon is a Java desktop application designed specifically for students of Ewha Womans University to explore and review restaurants near the campus. The application categorizes restaurants by different zones based on campus buildings, allowing for efficient and targeted searches. Students can quickly find and review dining options by category and location, with additional features to streamline their experience.

## ✨ Features
- **Filtering and Sorting**:
  - **Filtering**: Choose from 21 building zones ('ECC' to 'Ihouse') and 8 food categories ('Korean' to 'Cafe') for precise searches.
  - **Sorting**: Sort results by 'Order', 'Name', or 'Rating' after applying filters.

- **Real-time Bookmarking and Ratings**:
  - **Bookmarking**: Save favorite restaurants and view them in the 'My Info' section.
  - **Ratings**: Rate restaurants from 0 to 5 stars. Ratings are updated in real-time and shown as average scores.

- **Real-time Menu Updates**:
  - **Menu Management**: Instant updates for menu changes ensure current information.

- **Login and Logout Functionality**:
  - **User & Admin Interfaces**: Distinct interfaces for users and admins with secure login and logout options.

## 💻 Technologies Used
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

- **JAVA** - Core programming language for the application.
- **Swing & AWT** - creating the graphical user interface (GUI).
- **JDBC** - handling database interactions.
- **MySQL** - storing restaurant data.

## ⚙️ Installation & Setup

1. Clone this repository:
    ```bash
    git clone https://github.com/jiu31/ewha-ribbon.git
    ```

2. Install [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) if it is not already installed.

3. Set up the database:
   - Navigate to the `mysql` directory and execute the `create.sql` script to initialize the database:
     ```bash
     cd ewha-ribbon/mysql
     mysql -u [username] -p [password] < create.sql
     ```

4. Navigate to the `db/src/db` directory and compile the main Java file:
    ```bash
    cd ewha-ribbon/db/src/db
    javac DB2024Team13_main.java
    ```

5. Run the application:
    ```bash
    java DB2024Team13_main
    ```

Make sure that your MySQL server is running and configured to allow connections before running the app.

## 🗃️ Database Structure
EwhaRibbon uses a **relational database** to manage restaurant data. Key tables include:

- **restaurants**: Stores general restaurant information like name, address, and category.
- **menus**: Contains menu items for each restaurant.
- **reviews**: Allows users to write and read reviews of restaurants.
- **bookmarks**: Enables users to save restaurants. Stores the restaurant name and the user’s ID.

## 🖱️ How to Use
1. Launch the application.
2. Explore restaurants by selecting a zone or using the search function to filter results by cuisine, price, or ratings.
3. View detailed information for each restaurant.
4. Add ratings and ratings for the restaurants you’ve visited.

## 🤝 Contributing
If you would like to contribute to the project, feel free to fork the repository and submit a pull request.

## 📄 License
This project is licensed under the MIT License.
