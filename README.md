# webapp14

# Noir & Blanc

Noir & Blanc is an online clothing store where users can purchase products and leave reviews. Additionally, administrators can manage the catalog and analyze sales

### 👩‍👩‍👦‍👦 TEAM MEMBERS
| Name and surname    | URJC mail      | GitHub user      |
|:------------: |:------------:| :------------:|
| Ikram El Jauhari Al Jaouhari    | i.eljauhari.2022@alumnos.urjc.es      | ikramej          |
| Alberto Mayoral Gómez           | a.mayoral.2022@alumnos.urjc.es        | Albermg27        |
| Icíar Moreno López              | i.morenolo.2022@alumnos.urjc.es       | IciarML          |
| Jorge Ramírez Gayo              | j.ramirezg.2017@alumnos.urjc.es       | jorgeramirezgayo |

### 📝 ENTITIES
1. **User**: There are three types of users: Registered users, unregistered users and administrators
2. **Product**: Represents the items available in the store
3. **Order**: An order purchased by the user
4. **Review with score**: Allows registered users to leave a review about a product
   
![Image](https://github.com/user-attachments/assets/93a4bfab-4edd-4bb8-b684-b3a9bd4d0027)

### 🔐 USER PERMISSIONS
- **Registered users**: They can place orders, access their order history and leave reviews
- **Unregistered users**: They can view products but they can't place orders
- **Administrators**: They can add, edit, and delete products, as well as remove reviews and ban users

### 🖼️ IMAGES
- **Products**: Each product will have an image
- **Users**: Each user will have a profile picture

### 📊 GRAPHICS
- The administrator will be able to view a bar graph with the most purchased products
- The administrator will be able to view a line graph that displays the sales of the last month

### 💻 COMPLEMENTARY TECHNOLOGY
An email will be sent when a purchase is made and a pdf will be generated with the order details

### 💡 ADVANCED ALGORITHM
A set of suggested products is displayed based on the categories of products previously purchased by the user

### 📺 SCREENS
| Screen name    | Screen image      | Screen description      |
|:------------: |:------------:| :------------:|
| ![Image](https://github.com/user-attachments/assets/ee5f1a78-ca5b-444f-8926-90adb651caa7) | Login | This screen is used by users and administrators to login using their email and password |
| ![Image](https://github.com/user-attachments/assets/a1c7778a-d507-4234-a23b-a69ec2520b02) | Register | This screen is used to register users. Users must indicate their name and surname, their email and create a password |
| ![Image](https://github.com/user-attachments/assets/2d827b68-f10e-4422-be38-dfe19b4c7266) | Admin profile | Screen where the administrator can see their personal data |
| ![Image](https://github.com/user-attachments/assets/8db25f4c-b259-48e9-95f2-4f7a112d6097) | Admin edit profile | Screen where the administrator can edit their personal data (name and surname, email and password) |
| ![Image](https://github.com/user-attachments/assets/966c5628-2468-4547-928e-f333425ef0a1) | Admin charts | Screen where the administrator will be able to view a bar graph with the most purchased products and a line graph displaying the sales of the last month |
| ![Image](https://github.com/user-attachments/assets/a93f8d23-948f-492a-8ab1-b2aa8dc9c37a) | Admin users | Screen in which the administrator can see the users of the website, he/she can also see the reports that have been made and the users that are banned |
| ![Image](https://github.com/user-attachments/assets/18cdb7aa-590d-495f-bdd7-c1a5e844501b) | Admin orders | Screen where the administrator can see the orders that all users have made, their details and delete them |
| ![Image](https://github.com/user-attachments/assets/49bd1d6a-2e82-487e-9cd5-2a15de2e3817) | Admin products | Screen where the administrator can see all the products, their details, edit and delete them. The administrator can also observe the products that are or are not available |
| ![Image](https://github.com/user-attachments/assets/2c397c5d-1a7d-49ca-8475-eef11d5d31fd) | Users profile | Screen where registered users can edit certain attributes |
| ![Image](https://github.com/user-attachments/assets/6d788f7a-37ad-4284-ba56-463d2e89f937) | Index | Main screen of the website where users can view the different product categories |
| ![Image](https://github.com/user-attachments/assets/d0d0d840-908c-47cb-82c8-780360662375) | Category | Screen where users can view products belonging to a specific category |
| ![Image](https://github.com/user-attachments/assets/5966cffd-5353-48af-b078-baa9e0207ffc) | Elem detail | Screen where the information of a specific product is displayed |
| ![Image](https://github.com/user-attachments/assets/363d9bb7-5ab6-4e6f-9a93-0c289796d470) | Cart | Screen where the products added to the cart by a user are displayed |
| ![Image](https://github.com/user-attachments/assets/8daa2fca-96ac-4f7d-8c46-8406ea5497b5) | Orders | Screen where the previous orders made by a user are displayed |
| ![Image](https://github.com/user-attachments/assets/d191b5eb-82aa-4d7d-9aa5-9ac5e7bfeb48) | Orders detail | Screen where the details of the products belonging to a specific order are displayed |
| ![Image](https://github.com/user-attachments/assets/2a0228bb-a0ad-4417-9ebc-78dc8d28140a) | Access error | The screen displays an access error, indicating that the user does not have permission to view the content |
| ![Image](https://github.com/user-attachments/assets/00775d7c-3080-4429-9c2f-33688765dbe6) | No page error | The screen indicates that the requested page does not exist or is not available |

### 🗺️ NAVIGATION DIAGRAM

![Image](https://github.com/user-attachments/assets/26a10dbc-e39d-467c-b3f1-d4aafc4949c9)

### 📁 DIAGRAM WITH DATABASE ENTITIES

![Diagrama_BDEntidades](https://github.com/user-attachments/assets/ce8dfeef-4a3b-4d84-8887-7d775dc1ba18)

### 🗂️ CLASS AND TEMPLATES DIAGRAM

![image](https://github.com/user-attachments/assets/1806b785-853e-43bd-a312-32ce17661b32)

# PHASE 1

🛠️ ### EXECUTION INSTRUCTIONS

1.  **Download the repository:**
    
    -   Clone or download this repository to your local machine.
2.  **Install Java 21:**
    
    -   If you don't have Java 21 installed, you can download it from the following link:  
        [Download Java 21](https://www.oracle.com/es/java/technologies/downloads/#java21).
3.  **Set up the database:**
    
    -   We are using **XAMPP** for the database, but you can use any SQL editor you prefer.
    -   Create a database named `shop` and set the password `admin` for the `root` user.
    
    In the **SQL Shell**, run the following commands:
    
    ```sql
    CREATE DATABASE shop;
    ALTER USER 'root'@'localhost' IDENTIFIED BY 'admin';
    ```
    
    -   Then, go to the `config.inc.php` file (located in `xampp/phpMyAdmin`) and set the `root` user password to `admin`.
4.  **Run the project:**
    
    -   **Recommended:** Use **Visual Studio Code** and install the **Java Extension Pack**, **Spring Boot Extension Pack** and **Maven for Java** extensions. After that, simply click the "Run" button.
        
    -   Alternatively, you can install **Maven** manually to run the project. Navigate to the **backend** folder and run the following command:
    
    ```bash
    mvn spring-boot:run
    ```
    
5.  **Access the project:**
    
    -   Once the project is running, open your browser and go to:  
        [https://localhost:8443/index](https://localhost:8443/index)

### 🤝 PARTICIPATION

### **Ikram El Jauhari Al Jaouhari**
I have been mostly responsible for developing the functions related to the index screen for both no resgitered users and registered users including a variety of "load more" buttons with AJAX, the element detail screen, the edit user profile screen and the category screen in which I had to show specific products based on the category. Also, I have been responsible for the functions of creating and editing reviews on the element detail screen. In addition, I have participated in the developing of the security integration with the web. Last but not least, I have developed the advanced algorithm which consisted in showing more products based on the categories of the user last order
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|[Added all products to database and fixed index.html](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/139b29849c94a9bd06c49a3c10b91212d00e6ebe)|[ProductController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/ProductController.java)|
|2|[updated elem_detail.html and category.html part 1](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/8133c076dbba4c2b561d23bc90ec45bf91365508)|[UserController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/UserController.java)|
|3|[added loadMore button with AJAX](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/b9321ab640e7d64993f68d70f60812f0d1b1d3f3)|[app.js](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/resources/static/js/app.js)|
|4|[added Show Recommended Products algorithm](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/5e993f5c790495c36105eb2a972c2dfd003165e1)|[AdminController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/AdminController.java)|
|5|[Spring security (part 2) and added edit_profile form verifications](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/14fa4089f835b7dd7038a745e93b6b1262404964)|[WebSecurityConfig.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/security/WebSecurityConfig.java)|

### **Alberto Mayoral Gómez**
I have been responsible for developing the functions related to the creation and edition of products by the administrator, as well as user management and the control of reviews reported by customers. On the other hand, I have implemented the shopping cart logic on the website, taking into account the addition, removal, and processing of items, considering the available stock and possible concurrency issues, as well as the product search bar on the main page. Additionally, I have collaborated on the security aspect regarding access to certain pages, providing error and authentication pages. Finally, I have also contributed to correcting and improving the website in general
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|[Implemented Cart for logged Users](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/451bc92b4f76e17a67edfc6fcfcbd4449d64c9cf)|[AdminController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/AdminController.java)|
|2|[Spring Security (csrf / roles / permissions)](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/a05b59a55c7a317f7c9ca3385f30b18d0bda5452)|[CartController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/CartController.java)|
|3|[Edition and creation of products in admin_products.html](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/619b3a837053b913216274b51ff8ca5788f9465f)|[OrdersController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/OrdersController.java)|
|4|[Report and delete Reviews by Users in elem_detail.html and fixed errors](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/d2320e9950acddc2c049934fe5d5f33fae1b5c6c)|[ProductController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/ProductController.java)|
|5|[Admin_products.html view linked to the database and requests handled by the Controller](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/d8a346a4976d6556baaf967e24e75ad05276d7f7)|[admin_products.hmtl](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/resources/templates/admin/admin_products.html)|

### **Icíar Moreno López**
I have developed the login and registration screens, managing user authentication and storage. I have also implemented the admin profile management so he/she can update his/her information easily. In addition, I have developed the order management screen, working on the logic of its creation and making sure only paid orders are shown and allowing their status to be updated. I have also implemented the generation of PDFs with order details and the automatic sending of emails to users after making a purchase. Finally, I have corrected errors and improved general aspects of the website
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|[Add login, register and admin profile views with database support](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/47df586c7f8a5a662396ce25e79e16cae861ade0)|[AdminController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/AdminController.java)|
|2|[Added admin orders view with database support and fixed image saving issues](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/c8f61521d18fc10c3061426a77155bb799ca3cb3)|[UserController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/UserController.java)|
|3|[Added functionality to generate PDFs for orders](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/12cf5378fccbffcc4d93b94f3f8c37037d7351bf)|[OrdersController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/OrdersController.java)|
|4|[Added functionality to send emails](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/ad0fc2b2cd14adaccdf90b794368c7414b307f44)|[EmailService.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/services/EmailService.java)|
|5|[Fixed several errors in admin views](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/eacb1fbc9966ec57a254449c239d0904474849b7)|[admin_orders.html](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/resources/templates/admin/admin_orders.html)|

### **Jorge Ramírez Gayo**
Aaa
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|[Implemented charts](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/22a98d1cb0a5ddd0b0127303f02955d01010cd43)|[AdminController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/AdminController.java)|
|2|[Implemented order history and order details for users](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/0864798ca5bdb53958b77732a8efdcbff0395035)|[OrdersController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/controllers/OrdersController.java)|
|3|[Fixed data initialization](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/4d3a8ab128296c47fc2f5755603a08643951e3a1)|[DataInitializer.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/services/DataInitializer.java)|
|4|[Implemented basic classes](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/727f043c17ee8b75d28d65189400d50c33a59d8b)|[ProductController.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/727f043c17ee8b75d28d65189400d50c33a59d8b)|
|5|[Added size and order products](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/1ddc7ff92e8d50857654ecf235304806938f2025)|[OrderProduct.java](https://github.com/CodeURJC-DAW-2024-25/webapp14/blob/main/backend/src/main/java/es/codeurjc/webapp14/model/OrderProduct.java)|
