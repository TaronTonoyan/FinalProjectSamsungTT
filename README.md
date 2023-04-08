# Gaming Shop
This is my final project for Samsung Innovation Campus and Harvard CS50.

Email for Root: root@admin.com
Password for Root: Root

The project is a mobile application for a shopping platform that allows users to browse and purchase products from various categories. The application includes several activities and adapters that perform different functions like managing accounts, adding and removing admins, browsing products, managing cart and orders, viewing history, etc.

The first activity, LoginActivity, is the initial screen where users can enter their login credentials to access the application. If the user is not registered, they can go to the RegisterActivity to create a new account. Once the user logs in, they are directed to the MainActivity, which is the main menu for a regular user.

In the MainActivity, users can browse products from different categories, view their wishlist, and access their cart. Selecting a product category takes the user to the ProductCategoryActivity, where they can browse products in that category and add them to their cart or wishlist. Clicking on a specific product takes the user to the ProductActivity, where they can view the product's specifications and add it to their cart or wishlist.

The CartActivity displays all the items in the user's cart, including their quantity, price, and total cost. The user can update the quantity of a product or remove it from their cart. The CheckoutActivity allows the user to enter their address and payment details and complete the purchase.

The WishlistActivity displays all the items in the user's wishlist, and the user can add them to their cart or remove them from the wishlist. The GalleryActivity displays all the available products, and users can search for products using filters or sorting options.

The HistoryActivity displays the user's purchase history, including the products they bought, the date of purchase, and the total cost. The user can view the details of a specific order, including the products they purchased, the date of purchase, and the order status.

The AdminActivity is the main menu for the admin, and it allows them to add or remove admins (if they are root), add, update or delete products. The AddAdminsActivity allows the root user to add or remove admins from the application. The AccountSettingsActivity allows the user to change their password, address, or delete their account.

The application uses various adapters to display the data on the screen, including the AccountAdapter for the list of all the accounts other than root, CartAdapter for the list of all the items in the user's cart, GalleryAdapter for the list of all the products, and WishlistAdapter for the list of all the items in the user's wishlist.

The application stores all the user data, including accounts, products, orders, and history, in the DBShop database. The database uses encryption to protect the user's sensitive information, and it ensures the security of user data and prevents unauthorized access.

The project aims to provide a user-friendly shopping experience to its users while ensuring their security and privacy. By offering various features like browsing products, managing cart and orders, viewing history, and managing accounts, the application provides a comprehensive shopping platform that caters to the user's needs.
