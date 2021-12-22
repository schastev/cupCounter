# TastyCount
#### Video Demo:  <URL HERE>
#### Description:
This Android app is a helper app for any shop that provides a free something to the customer once the customer has purchased enough of those same somethings. The idea was inspired by a local coffee shop that used Google Sheets for this very purpose.
##### This app has:
1. Room database with the following structure: 
    *  customer ID
    *  their first name
    *  their phone number
    *  the number of somethings they have purchased from the store
    *  date of registration
    *  date of last update (called 'last visit' in the UI).
1. Main Screen where the user can search the database for customers by last digits of their phone number
1. New Customer screen where the user can add new customers. User's input is validated: only digits are allowed for the phone number field and only Russian OR English symbols can be saved in the name field
1. Customer Info screen which displays the information about the customer. It will show alerts when the customer is eligible for a freebie and if their database entry has not been updated in a certain number of days. The user can update the number somethings the customer purchased (both add and subtract though claiming), and edit customer's phone number
1. Settings screen where the user can customize eligibility criteria for the freebie, set the number of days for the alert to pop up. The user can also delete a customer from the database. This screen is meant to be used only by the shop manager and therefore is password-protected
1. Russian and English locales

##### Quality of life features:
1. if a search query returns only one customer, their info page is opened automatically
1. Update and Revert Changes buttons on the Customer Info screen only show up if the user changed the number of somethings the customer purchased
1. the user can show or hide the dates of registration or last visit on the Customer Info screen
1. the user can decide if one something should be automatically added to a customer upon registration

##### Project Structure:
1.  `app/manifests` contains general information about the app, description of its Activities (screens) 
1.  `app/java/com.example.cupcounter` contains main classes used in this app:
    * `/activity` stores classes for the four main Activities of the app. Each of them contains methods responsible for the initialization of UI elements on screen, representation and handling of data
    * `/customerlist` stores helper files to display search results on the main screen in a specific format
    * `/database` stores all helper classes needed to work with the Room database:
        * `AppDatabase` is a class describing the database itsel 
        * `Customer` is a class describing a Customer
        * `CustomerDAO` is an interface that allows to make changes to the Customer counterparts in the database
        * `DBClient` is a client that allows the Activities to communicate with the database and provides a single copy of the database to all parts of the app
        * `LocalDateTypeConverter` is a simple converter for dates (from LocalDate to String and vise-versa)
    * `/fragments` stores helper fragments (for Customer Info and Settings screens). They help organize the UI and simplify its editing. Dialog fragment is responsible for the pop-up window when the user wants to edit customer's phone number
    * `/toolbar` contains a class that is responsible for drawing a toolbar with only required buttons. The idea is to let user go from any Activity to any other Activity in the app, but I don't want a link to the Settings screen when I am already in there
    * `CustomerGenerator` is a class that populates the database with random customers with random dates of registration and last visit
1.  `app/res` contains .xml and other resources required for the app to work:
    * `drawable`, `mipmap` - icons for the UI and app itself
    * `layout` - descriptions of elements contained in each activity/fragment and their relationships between each other
    * `menu` - variations of button combinations for the toolbar
    * `values` - colors and strings used throughout the app both in UI and code only. I tried to avoid passing strings directly to make my life easier in the future if I ever need to edit a parameter or a constant name
    * `xml` - contains a list of preferences on the Settings screen. There is a commented-out category for database backup and restore, which is a planned feature that was discussed with the coffee shop but goes beyond the scope of this final project.

Your README.md file should be minimally multiple paragraphs in length, and should explain what your project is, 
what each of the files you wrote for the project contains and does, 
and if you debated certain design choices, explaining why you made them. 
Ensure you allocate sufficient time and energy to writing a README.md that you are proud of and that documents your project thoroughly. Be proud of it!
