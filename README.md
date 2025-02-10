# webapp14

## Noir & Blanc

Mode Royale is an online clothing store where users can purchase products and leave reviews. Additionally, administrators can manage the catalog and analyze sales

### 👩‍👩‍👦‍👦 Team members
| Name and surname    | URJC mail      | GitHub user      |
|:------------: |:------------:| :------------:|
| Ikram El Jauhari Al Jaouhari       | i.eljauhari.2022@alumnos.urjc.es       | ikramej       |
| Alberto Mayoral Gómez       | a.mayoral.2022@alumnos.urjc.es       | Albermg27       |
| Icíar Moreno López       | i.morenolo.2022@alumnos.urjc.es       | IciarML       |
| Jorge Ramírez Gayo       | j.ramirezg.2017@alumnos.urjc.es       | jorgeramirezgayo       |

### 🖥️ Entities
1. **User**: There are three types of users: Registered users, unregistered users and administrators
2. **Product**: Represents the items available in the store
3. **Order**: An order purchased by the user
4. **Review with score**: Allows registered users to leave a review about a product
![Image](https://github.com/user-attachments/assets/93a4bfab-4edd-4bb8-b684-b3a9bd4d0027)

### 🧩 Class diagram
![Image](https://github.com/user-attachments/assets/fe62c4fa-7021-4bc3-8a2b-a99d9fca3aea)

### 👨‍👩‍👦 User permissions
- **Registered users**: They can place orders, access their order history and leave reviews
- **Unregistered users**: They can view products but they can't place orders
- **Administrators**: They can add, edit, and delete products, as well as remove reviews and ban users

### 🖼️ Images
- **Products**: Each product will have an image
- **Users**: Each user will have a profile picture

### 📊 Graphics
- The administrator will be able to view a bar graph with the most purchased products
- The administrator will be able to view a line graph that displays the sales of the last month

### 💻 Complementary Technology
An email will be sent when a purchase is made and a pdf will be generated with the order details

### 💡 Advanced algorithm
A set of suggested products is displayed based on the categories of products previously purchased by the user

### 📺 Screens
| Screen name    | Screen image      | Screen description      |
|:------------: |:------------:| :------------:|
| ![Image](https://github.com/user-attachments/assets/25b042a9-b51d-4d4b-8c0e-0f0b90c65f65)       | Admin charts       | Screen where the administrator will be able to view a bar graph with the most purchased products and a line graph displaying the sales of the last month       |
| ![Image](https://github.com/user-attachments/assets/cdcc2834-227d-4be3-9871-8fd3de68786a)       | Admin orders       | Screen where the administrator can see the orders that all users have made and their details       |
| ![Image](https://github.com/user-attachments/assets/5584c478-3c4c-4b7a-8937-1b5f3e3f9057)       | Admin profile       | Screen where the administrator can edit their personal data (name and surname, email and password)       |
| ![Image](https://github.com/user-attachments/assets/ded15f09-3292-44ab-9c1e-836403ef712d)       | Admin products       | Screen where the administrator can see all the products, their details, edit and delete them. The administrator can also observe the products that are or are not available       |
| ![Image](https://github.com/user-attachments/assets/4520a856-d820-4c05-ab25-24b432da9281)       | Admin users       | Screen in which the administrator can see the users of the website, he/she can also see the reports that have been made and the users that are banned       |
| ![Image](https://github.com/user-attachments/assets/cd51105b-2c8e-4f92-94a7-ff5c5b5fc577)       | Login       | This screen is used by both users and administrators to login using their email and password      |
| ![Image](https://github.com/user-attachments/assets/b0619823-9d74-4dc8-9653-a0af8bbcd062)       | Register       | This screen is used to register users. Users must indicate their name, their email and create a password       |

