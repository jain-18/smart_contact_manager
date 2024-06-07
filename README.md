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

Images :- 
![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/d16a600a-30cc-49af-be72-b3a41b65495e)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/f97cd562-1d5c-4f9d-b97b-ae26e5a1c568)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/70e4946d-8c33-469e-91ac-f6a7cd5b0826)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/8d2458b3-c73a-4f12-bd76-f5502ffc09aa)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/5d3417ae-d736-4edd-bfd2-28cb728cf345)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/c03e7087-123a-42ba-abf3-649a9449e9b3)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/01c23577-d06b-4ad8-be66-8e4cda5f7ed2)

![image](https://github.com/jain-18/smart_contact_manager/assets/134261396/dfa3cb48-e17b-40f8-a2bb-c0f012e739df)




Thank you for using Smart Contact Manager!
