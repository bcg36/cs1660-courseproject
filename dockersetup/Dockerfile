#FROM maven:3.5.0-jdk-8
#COPY . /usr/src/myapp
#WORKDIR /usr/src/myapp
#RUN javac MyGUI.java
#CMD ["java", "MyGUI"]

FROM maven:3.5.0-jdk-8
WORKDIR /apps
COPY . /apps
RUN mvn clean install
CMD mvn exec:java -Dexec.mainClass="com.journaldev.maven.classes.MyGUI"