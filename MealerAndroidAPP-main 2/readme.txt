SEG 2105 B Project Deliverable 2 Student Names: Engy Elsayed, Brevin Baskaran, Shavon Thadani, Shaylin Thadani Student numbers (respectively): 300228400, 300231269, 300225889, 300225897

In this deliverable, we implemented the Administrator functionality. The app allows the Administrator to view a list of complaints. For each complaint, the Administrator can either dismiss the complaint or suspend the Cook associated with the complaint temporarily or indefinitely. Once the Administrator actions the complaint (dismisses or suspends), then the complaint disappears from the list of complaints. The complaints are not hard coded. They are stored in a DB. We pre-create a list of complaints stored on the Firebase DB on Firebase. Each complaint is associated with a registered Cook. If an Administrator suspends a Cook based on a complaint, then once that Cook logs on, they see a message that informs them that their account is suspended and when the suspension will be lifted in the case of temporary suspension. We included additional unit test cases (simple local tests).

Credentials to sign in as Adminstrator: Log in: admin1@gmail.com Password: admin123

These are the chefs displayed on the complaint page for the admin. If you would like to test out our complaint functionality, as an admin, long press on one of the chefs and select one of the three buttons to dismiss, temporarily suspend for 24 hours, or permanently ban. Then try to log in with the credentials for said chef to see the functionality of these options. Thanks!

email: chef1@gmail.com password: chef123

email: chef2@gmail.com password: chef123

email: chef3@gmail.com password: chef123

Github Link: https://github.com/MealerGroup21/MealerAndroidAPP 
