# docu-view

Document management app

## Node Install

### Mac / Windows

Download [installer](https://nodejs.org/en/download) and follow the instructions to install.

### Linux (ubuntu distros)

``sudo apt-get update; sudo apt install nodejs``

## Java Install

### Mac / Windows

Download [installer](https://www.java.com/en/download/) and follow the instructions to install.

### Linux (ubuntu distros)

``sudo apt update ; sudo apt install openjdk-21-jdk -y ; sudo update-java-alternatives -s $(update-java-alternatives -l | tr ' ' '\n' | grep java-1.21 | grep -v /)``

## Maven Install

### Mac

``brew install maven``

### Windows

If java has been downloaded, maven should be installed as well. If not follow the [instructions](https://maven.apache.org/install.html)

Then [setup](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)

### Linux (ubuntu distros)

``sudo apt-get update; sudo apt-get -y install maven``

## MySql Workbench Install

Download [installer](https://dev.mysql.com/downloads/workbench/) and follow the instructions to install.

## Frontend (React)

### Requirements:

* Node 18

### Commands (Windows)

``cd .\frontend ; npm install; cd .. ; npm start --prefix .\frontend``

### Commands (Linux/Mac):

``cd ./frontend ; npm install; cd .. ; npm start --prefix ./frontend``

## Server (Springboot)

### Requirements:

* Java 21
* Maven 3.9.4
* Spring Boot 3.1.4

### Commands (Windows):

Note: setup a env variable JAVA_HOME to the main dirrectory where java is installed
start server from inside `.server`  dir

``$env:JAVA_TOOL_OPTIONS = "$env:JAVA_TOOL_OPTIONS -Djdk.jar.maxSignatureFileSize=30806623"; mvn package -f pom.xml; .\mvnw.cmd -f pom.xml spring-boot:run``

### Commands (Linux):

Note: before executing, exectute ``chmod +x server/mvnw`` to allow file to executed
start server from parent dir
``export _JAVA_OPTIONS="$_JAVA_OPTIONS -Djdk.jar.maxSignatureFileSize=30806623"; export JAVA_HOME=/usr ; mvn package -f server/pom.xml ; ./mvnw -f server/pom.xml spring-boot:run``

### Commands (Mac)

Note: before executing, exectute ``chmod +x server/mvnw`` to allow file to executed
start server from parent dir
``export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home ; server/mvnw -f server/pom.xml spring-boot:run``

## Database (MySQL)

### Requirements:

* MySql workbench
* digi certify file
