# facilitron
**Project Member:**
- [Timo Kappel (TK Inc. aka Smoking Skull)](https://github.com/Caperino)
- [Stefan Jöbstl (Juan aka The Train)](https://github.com/stefanjb-it)
- [Huba Csicsics (Meatman aka Human Socerie)](https://github.com/einfachhuba)
- [Alexander Wolf (UI Wizard aka Ungünstig)](https://github.com/AlexW64)

# Setup

## Software requirements:
- MySQL 8.0 or higher
- Java JDK 17 or higher
- IntelliJ
- Tomcat 9 or higher
- Kotlin language support
- Mailhog

## Files:
- application.properties:
```
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:mysql://localhost:3306/facilitron
spring.datasource.username=facilitron_backend
spring.datasource.password=highlysecurepassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#spring.jpa.show-sql: true
spring.mail.host=localhost
spring.mail.port=1025
```

## Steps:
- Create database with name facilitron
- Create new user and grant all rights on facilitron database
- Copy application.properties and change url, username or password if required
- Start mailhog
- Run spring boot application in IntelliJ
