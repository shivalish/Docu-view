# Docu-view

Document management app

## Java Install

### Windows / Mac

Download [installer](https://www.java.com/en/download/)
Follow the instructions to install

### Linux (ubuntu distros)

``sudo apt update ; sudo apt install openjdk-21-jdk -y ; sudo update-java-alternatives -s $(update-java-alternatives -l | tr ' ' '\n' | grep java-1.21 | grep -v /)``

## Maven Install

### Windows

If java has been downloaded, maven should be installed as well. If not follow the next instructions:
[https://maven.apache.org/guides/getting-started/windows-prerequisites.html](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)
[https://maven.apache.org/install.html](https://maven.apache.org/install.html)


### Mac

``brew install maven``

### Linux (ubuntu distros)

``sudo apt-get update; sudo apt-get -y install maven``

## Node Install

### Windows / Mac

Download [installer
](https://nodejs.org/en/download)Follow the instructions to install

### Linux (ubuntu distros)

`sudo apt update;` `sudo apt -y install nodejs`

## Frontend (React)

### Requirements:

* Node 18

### Commands (Windows/Linux/Mac):

``npm install --prefix frontend/docuview-react-app ; npm start --prefix frontend/docuview-react-app``

## Server (Springboot, Tomcat)

### Requirements:

* Java 21
* Maven 3.9.4
* Spring Boot 3.1.4

### Commands (Windows):

``set JAVA_HOME=C:\Program Files\Java\jdk-21 ; server\mvnw.cmd -f server\pom.xml spring-boot:run``

### Commands (Linux):

``export JAVA_HOME=/usr ; server/mvnw -f server/pom.xml spring-boot:run``

### Commands (Mac)

``export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home ; server/mvnw -f server/pom.xml spring-boot:run``

## Database (MySQL)

### Requirements:

* MySql workbench
* digi certify file
