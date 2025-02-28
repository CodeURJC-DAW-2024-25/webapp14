# webapp14

# Noir & Blanc

Mode Royale is an online clothing store where users can purchase products and leave reviews. Additionally, administrators can manage the catalog and analyze sales

### 👩‍👩‍👦‍👦 TEAM MEMBERS
| Name and surname    | URJC mail      | GitHub user      |
|:------------: |:------------:| :------------:|
| Ikram El Jauhari Al Jaouhari       | i.eljauhari.2022@alumnos.urjc.es       | ikramej       |
| Alberto Mayoral Gómez       | a.mayoral.2022@alumnos.urjc.es       | Albermg27       |
| Icíar Moreno López       | i.morenolo.2022@alumnos.urjc.es       | IciarML       |
| Jorge Ramírez Gayo       | j.ramirezg.2017@alumnos.urjc.es       | jorgeramirezgayo       |

### 📝 ENTITIES
1. **User**: There are three types of users: Registered users, unregistered users and administrators
2. **Product**: Represents the items available in the store
3. **Order**: An order purchased by the user
4. **Review with score**: Allows registered users to leave a review about a product
   
![Image](https://github.com/user-attachments/assets/93a4bfab-4edd-4bb8-b684-b3a9bd4d0027)

### 🧩 CLASS DIAGRAM

![Image](https://github.com/user-attachments/assets/fe62c4fa-7021-4bc3-8a2b-a99d9fca3aea)

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
| ![Image](https://github.com/user-attachments/assets/25b042a9-b51d-4d4b-8c0e-0f0b90c65f65) | Admin charts | Screen where the administrator will be able to view a bar graph with the most purchased products and a line graph displaying the sales of the last month |
| ![Image](https://github.com/user-attachments/assets/cdcc2834-227d-4be3-9871-8fd3de68786a) | Admin orders | Screen where the administrator can see the orders that all users have made and their details |
| ![Image](https://github.com/user-attachments/assets/5584c478-3c4c-4b7a-8937-1b5f3e3f9057) | Admin profile | Screen where the administrator can see their personal data |
| ------------------------------------ | Admin edit profile | Screen where the administrator can edit their personal data (name and surname, email and password) |
| ![Image](https://github.com/user-attachments/assets/ded15f09-3292-44ab-9c1e-836403ef712d) | Admin products | Screen where the administrator can see all the products, their details, edit and delete them. The administrator can also observe the products that are or are not available |
| ![Image](https://github.com/user-attachments/assets/4520a856-d820-4c05-ab25-24b432da9281) | Admin users | Screen in which the administrator can see the users of the website, he/she can also see the reports that have been made and the users that are banned |
| ![Image](https://github.com/user-attachments/assets/cd51105b-2c8e-4f92-94a7-ff5c5b5fc577) | Login | This screen is used by both users and administrators to login using their email and password |
| ![Image](https://github.com/user-attachments/assets/b0619823-9d74-4dc8-9653-a0af8bbcd062) | Register | This screen is used to register users. Users must indicate their name, their email and create a password |
| ![Image](https://github.com/user-attachments/assets/d09c31de-a73b-4bd7-8e42-55bd673df736) | Index | Main screen of the website where users view the different product categories |
| ![Image](https://github.com/user-attachments/assets/b46f73dd-24a1-42ea-ad59-0a5da37da4be) | Users profile | Screen where registered users can edit certain attributes |
| ![Image](https://github.com/user-attachments/assets/caf08c29-3399-4161-9ba2-cb6c7915b3f9) | Category | Screen where users can view products belonging to a specific category |
| ![Image)](https://github.com/user-attachments/assets/aaff7dd2-0d46-4a93-87a0-8f838831b050) | Elem detail | Screen where the information of a specific product is displayed |
| ![Image](https://github.com/user-attachments/assets/34279293-2c28-492c-9d88-1a143c7f3066) | Cart | Screen where the products added to the cart by a user are displayed |
| ![Image](https://github.com/user-attachments/assets/dd6c566a-f78b-4605-9629-248ab883637b) | Orders | Screen where the previous orders made by a user are displayed |
| ![Image](https://github.com/user-attachments/assets/e2eb1a38-7ee4-4cc4-9a1d-f806c7e4542a) | Orders detail | Screen where the details of the products belonging to a specific order are displayed |

### 🗺️ NAVIGATION DIAGRAM

![Image](https://github.com/user-attachments/assets/d030c6fd-b1e1-49f8-b9ea-3604ee3be4da)

### 📁 DIAGRAM WITH DATABASE ENTITIES

![Diagrama_BDEntidades](https://github.com/user-attachments/assets/ce8dfeef-4a3b-4d84-8887-7d775dc1ba18)

### 🗂️ CLASS AND TEMPLATES DIAGRAM
![image](https://github.com/user-attachments/assets/f22ceb70-2a3e-4610-a0b0-4ebd72856ce7)

# PHASE 1

## 🤝 PARTICIPATION

### **Ikram El Jauhari Al Jaouhari**
Aaa
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|   |   |
|2|   |   |
|3|   |   |
|4|   |   |
|5|   |   |

### **Alberto Mayoral Gómez**
I have been responsible for developing the functions related to the creation and edition of products by the administrator, as well as user management and the control of reviews reported by customers. On the other hand, I have implemented the shopping cart logic on the website, taking into account the addition, removal, and processing of items, considering the available stock and possible concurrency issues, as well as the product search bar on the main page. Additionally, I have collaborated on the security aspect regarding access to certain pages, providing error and authentication pages. Finally, I have also contributed to correcting and improving the website in general.
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|[Implemented Cart for logged Users](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/451bc92b4f76e17a67edfc6fcfcbd4449d64c9cf)   |AdminController.java|
|2|[Spring Security (csrf / roles / permissions)](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/a05b59a55c7a317f7c9ca3385f30b18d0bda5452)   |CartController.java|
|3|[Edition and creation of products in admin_products.html](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/619b3a837053b913216274b51ff8ca5788f9465f)   |OrdersController.java|
|4|[Report and delete Reviews by Users in elem_detail.html and fixed errors](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/d2320e9950acddc2c049934fe5d5f33fae1b5c6c)   |ProductController.java|
|5|[Admin_products.html view linked to the database and requests handled by the Controller](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/d8a346a4976d6556baaf967e24e75ad05276d7f7)   |admin_products.hmtl|

### **Icíar Moreno López**
I have developed the login and registration screens, managing user authentication and storage. I have also implemented the admin profile management so he/she can update his/her information easily. In addition, I have developed the order management screen, working on the logic of its creation and making sure only paid orders are shown and allowing their status to be updated. I have also implemented the generation of PDFs with order details and the automatic sending of emails to users after making a purchase. Finally, I have corrected errors and improved general aspects of the website
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|[Add login, register and admin profile views with database support](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/47df586c7f8a5a662396ce25e79e16cae861ade0)|AdminController.java|
|2|[Added admin orders view with database support and fixed image saving issues](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/c8f61521d18fc10c3061426a77155bb799ca3cb3)|UserController.java|
|3|[Added functionality to generate PDFs for orders](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/12cf5378fccbffcc4d93b94f3f8c37037d7351bf)|OrdersController.java|
|4|[Added functionality to send emails](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/ad0fc2b2cd14adaccdf90b794368c7414b307f44)|EmailService.java|
|5|[Fixed several errors in admin views](https://github.com/CodeURJC-DAW-2024-25/webapp14/commit/eacb1fbc9966ec57a254449c239d0904474849b7)|Admin_orders.html|

### **Jorge Ramírez Gayo**
Aaa
| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1|   |   |
|2|   |   |
|3|   |   |
|4|   |   |
|5|   |   |
