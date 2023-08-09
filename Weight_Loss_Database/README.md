## Enhancement Three: Databases ##

### Artifact Description ###
The chosen artifact for Milestone 4 is the Weight Loss app designed for CS-360, developed last term over the course of 8 weeks, from March to June 2023. It is an Android mobile application written in Java. The app serves as a weight loss tool, allowing users to log their weight, set goals, create user accounts, and manage weight-related information. It was implemented in Java and integrated with a default SQLite database for data storage.

### Justification for Inclusion
I have selected this artifact for inclusion in my ePortfolio because it demonstrates my software development skills, particularly in database management and security. The artifact highlights my ability to address critical issues in the original database implementation and improve the app's overall functionality and user experience.

### Feedback Incorporation and Artifact Improvement
The artifact underwent significant improvement through a series of enhancements planned in Module One. The initial database implementation lacked essential security features, such as input validation, encryption, and modularity. The database was also written in a non-modular manner, leading to less readable code and insufficient abstraction. To address these issues, I followed the enhancement plan and executed the following:

**Database Management:** I demonstrated my proficiency in database management by creating a separate DatabaseHelper class to handle interactions with the SQLite database. This enhancement encapsulates the database functionality and promotes modularity. This change enabled making the codebase more organized and maintainable.

**Input Validation and Sanitization:** The artifact showcases my understanding of data integrity and security. I implemented logic to sanitize and validate all user input before storing it in the database or checking it against account credentials. This enhancement mitigates potential vulnerabilities arising from incorrect or malicious input. Previously, there was no validation or sanitization, allowing users to create accounts with no login information and log weights consisting of non-integers. Now, every input is properly sanitized and validated before interfacing with the rest of the application.

**Password Security:** While the original implementation lacked encryption and security for passwords, I enhanced the app's security by implementing password hashing and salting techniques using SHA-256 hashing and a unique salt value. This improvement protects user passwords, even if the database file is compromised. Before this enhancement, user information was stored in plaintext.
### Alignment with Course Objectives
The planned enhancements align with the course outcomes, particularly the outcome of: "Develop a security mindset that anticipates adversarial exploits in software architecture and designs to expose potential vulnerabilities, mitigate design flaws, and ensure privacy and enhanced security of data and resources."

By addressing the lack of security features in the initial database implementation, I demonstrated my understanding of secure coding practices and data protection. The improved artifact effectively showcases my ability to implement secure and efficient database solutions and meets desired course outcomes for this enhancement.