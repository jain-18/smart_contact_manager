# smart_contact_manager# Smart Contact Manager

## Overview
Smart Contact Manager is a robust web application designed to manage contacts efficiently. Users can store contact information, including images, emails, and descriptions. The application includes comprehensive features such as contact management, user authentication, and password recovery via OTP.

## Features
- **Contact Management**: Users can add, update, and delete contacts. Each contact can include a name, email, image, and description.
- **User Authentication**: Secure user sign-up and login functionality.
- **Password Recovery**: Users can reset their forgotten passwords through an OTP sent to their registered email.
- **Validation**: All forms include validation to ensure data integrity.
- **Responsive Design**: User-friendly interface designed with Thymeleaf, CSS, and JavaScript.

## Technology Stack
- **Backend**: Spring Boot, Hibernate
- **Frontend**: Spring MVC, Thymeleaf, CSS, JavaScript
- **Database**: SQL

## Installation
1. **Clone the repository**
    ```sh
    git clone https://github.com/jain-18/smart_contact_manager.git
    cd smart_contact_manager
    ```

2. **Configure the database**
    - Update the `application.properties` file with your SQL database credentials.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/contact_manager
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    ```


## Usage
1. **Sign Up**: Register a new user account.
2. **Login**: Access your account by logging in with your credentials.
3. **Add Contact**: Navigate to the 'Add Contact' section and fill in the contact details.
4. **Update/Delete Contact**: Edit or remove contacts from your contact list.
5. **Password Recovery**: Use the 'Forgot Password' feature to reset your password via OTP.

## Contributing
We welcome contributions to enhance the Smart Contact Manager. Please fork the repository and submit a pull request.



Thank you for using Smart Contact Manager!
