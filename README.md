# docu-view

Document management app

## JAVA INSTALL FOR UBUNTU DISTROS

``` sudo apt install openjdk-21-jdk ; sudo update-java-alternatives -s $(update-java-alternatives -l | tr ' ' '\n' | grep java-1.21 | grep -v /) ```

## Maven Install

### Mac

```brew install maven```

### Windows

If java has been downloaded, maven should be installed as well. If not follow the next instructions:

[https://maven.apache.org/install.html]

Then setup

[https://maven.apache.org/guides/getting-started/windows-prerequisites.html]

### Linux

``` sudo apt-get update ```

## Frontend (React)

### Commands (Windows/Linux/Mac):
start application from parent directory \
``` npm install --prefix frontend/docuview-react-app ; npm start --prefix frontend/docuview-react-app```

### Requirements:

* Node 18

## Server (Springboot)

### Commands (Windows):
Note, setup a env varible JAVA_HOME to the main dirrectory where java is installed
start server from inside parent dir \
``` server\mvnw.cmd -f server\pom.xml spring-boot:run ```
### Commands (Linux/Mac):

start server from parent dir \
``` export JAVA_HOME=/usr ; server/mvnw -f server/pom.xml spring-boot:run ```

### Requirements:

* Java 21
* Maven 3.9.4
* Spring Boot 3.1.4

## Database (MySQL)

### Requirements:

* MySql workbench
* digi certify file
