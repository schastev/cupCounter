# TastyCount

#### Video Demo:  <URL HERE>

#### Description:

This Android app is a helper app for any shop that provides a free drink to the customer once
the customer has purchased enough of those same drinks. The idea was inspired by a local coffee
shop that used Google Sheets for this very purpose.

##### This app has:

1. Room database with the following structure:
    * customer ID
    * their first name
    * their phone number
    * the number of drinks they have purchased from the shop
    * date of registration
    * date of last update (called 'last visit' in the UI).
1. Main Screen where the user can search the database for customers by last digits of their phone
   number
1. New Customer screen where the user can add new customers. User's input is validated: only digits
   are allowed for the phone number field and only Russian OR English symbols can be saved in the
   name field
1. Customer Info screen which displays the information about the customer. It will show alerts when
   the customer is eligible for a freebie and if their database entry has not been updated in a
   certain number of days. The user can update the number drinks the customer purchased (both
   add and subtract though claiming), and edit customer's phone number
1. Settings screen where the user can customize eligibility criteria for the freebie, set the number
   of days for the alert to pop up. The user can also delete a customer from the database. This
   screen is meant to be used only by the shop manager and is therefore password-protected. The
   password can be changed on this same screen
1. Russian and English locales

##### Quality of life features

1. if a search query returns only one customer, their info page is opened automatically
1. Update and Revert Changes buttons on the Customer Info screen only show up if the user changed
   the number of drinks the customer purchased
1. the user can show or hide the dates of registration or last visit on the Customer Info screen
1. the user can decide if one drink should be automatically added to a customer upon
   registration

##### Project Structure

1. `app/manifests` contains general information about the app, description of its Activities (
   screens)
1. `app/java/com.tasty.count` contains main classes used in this app:
    * `/activity` stores classes for the four main Activities of the app. Each of them contains
      methods responsible for the initialization of UI elements on screen, representation and
      handling of data
    * `/customerlist` stores helper files to display search results on the main screen in a specific
      format
    * `/database` stores all helper classes needed to work with the Room database:
        * `AppDatabase` is a class describing the database itself
        * `Customer` is a class describing a Customer
        * `CustomerDAO` is an interface that allows to make changes to the Customer counterparts in
          the database
        * `DBClient` is a client that allows the Activities to communicate with the database and
          provides a single copy of the database to all parts of the app
        * `LocalDateTypeConverter` is a simple converter for dates (from LocalDate to String and
          vise-versa)
    * `/fragments` stores helper fragments. They help organize the UI and simplify its editing
        * `DialogFragment` is responsible for the pop-up window when the user wants to edit
          customer's phone number
        * `InfoDisplayFragment` stores a template of a text field for the Customer info screen
        * `PasswordPromptFragment` is responsible for asking the user for the Admin password when
          entering Settings. It also checks it against the value stored in memory
        * `SettingsFragment` is responsible for handling and saving all of the app's preferences
        * `SetPasswordFragment` is responsible for changing Admin password. It asks for current
          password, and if it is correct, allows the user to input a new password. After validating
          it as it is typed and making sure it matches the input in the confirmation field, it
          allows the user to click "Save"
    * `/toolbar` contains a class that is responsible for drawing a toolbar with only required
      buttons. The idea is to let user go from any Activity to any other Activity in the app, but I
      don't want the user to see a link to the Settings screen when they are already there
    * `CustomerGenerator` is a class that populates the database with random customers with random
      dates of registration and last visit. Mainly there for testing purposes
    * `Validator` is a class responsible for validating user input
1. `app/java/com.tasty.count (androidTest)` contains a class with basic database tests
1. `app/res` contains .xml and other resources required for the app to work:
    * `drawable`, `mipmap` - icons for the UI and app itself
    * `layout` - descriptions of elements contained in each activity/fragment and their positions
      relative to each other and screen proportions
    * `menu` - variations of button combinations for the toolbar
    * `values` - colors and strings used throughout the app both in UI and code only. I tried to
      avoid passing strings directly to make my life easier in the future if I ever need to edit a
      parameter or a constant name
    * `xml` - contains a list of preferences on the Settings screen.

##### Design choices

1. `InfoDisplayFragment` had two default TextViews which I put into a fragment to save some time on
   formatting and styling. Afterwards I discovered Material TextInputLayout which basically takes
   care of all the formatting. So I could have gone back to a simpler structure and place four
   identical TextInputLayouts into an xml, but decided not to, because these fragments were a good
   exercise in displaying different pieces of information in different fragments instantiated from
   the same class.
1. Dates of last visit and registration were initially always visible, but after consulting a
   barista, it was decided that this information is not essential, so the switch to hide them was
   added to the settings screen. After thinking about it some more, I decided to make this setting
   accessible to the non-admin users as well, since the dates may be required sometimes.
1. Toggle for auto +1 for new customers was also initially on the Settings screen and available only
   to administrators. But after giving it some thought, I moved it to the New Customer screen: there
   may be situations where the user needs to use both states of the toggle in a short period and the
   admin may not be there to change it. The default value was set to Off: the user can add one
   drink really easily, but subtracting exactly one will require some effort and help from an
   administrator.
