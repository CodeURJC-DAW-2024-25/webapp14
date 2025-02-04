# webapp14

## Mode Royale

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
4. **Purchase**: Represents the user's cart
5. **Review with score**: Allows registered users to leave a review about a product

### 🧩 Class diagram
![image](https://github.com/user-attachments/assets/ede7b575-3474-4d34-ad30-5c0fce0902f1)

### 👨‍👩‍👦 User permissions
- **Registered users**: They an place orders, access their order history and leave reviews
- **Unregistered users**: They can view products but they can't place orders
- **Administrators**: They can add, edit, and delete products, as well as remove reviews

### 🖼️ Images
- **Products**: Each product will have an image
- **Users**: Each user will have a profile picture

### 📊 Graphics
- The administrator will be able to view a bar graph with the most purchased products
- The administrator will be able to view a line graph that displays the sales of the last month
- The administrator will be able to ban users


### 💻 Complementary Technology
An email will be sent when a purchase is made

### 💡 Advanced algorithm
A set of suggested products is displayed based on the categories of products previously purchased by the user
