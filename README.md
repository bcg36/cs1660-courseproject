# cs1660-courseproject

VIDEO WALKTHROUGH LINK: https://www.youtube.com/channel/UCZt6u-RpkO6So0g5bvJ1ZyQ And select "1660 Code Walkthrough"

Requirements Completed:
1. Edit WordCount to do Inverted Indexing
2. Get My Inverted Indexing Running on GCP

Explaination of Other Requirements Not Working/ Partially working:
1. Getting GUI running on docker
  -I have completed a Dockerfile in which you can see in InvertedIndex folder of the github. It executes pulls from the Maven/JDK 8 from docker hub and it builds correctly. Unfortunately, when you run the run command it runs the clean install of maven and then tries to run my program with the IP that I give it but it says thay my display variable is not set correctly. You can see the IP I set it to and the fact that I have XQuartz running on my Mac in the demo video. The docker command I run are as follows:
  
docker build -t mygui .
  
docker run -it --privileged -e DISPLAY=10.0.0.123:0 -v /tmp/.X11-unix:/tmp/.X11-unix -e GOOGLE_APPLICATION_CREDENTIALS=/Users/ben/Desktop/bcg36-credentials.json mygui

The run command is set with my IP as display and the local path I had to my credentials which I have not included in my submission.

2. Getting Communication between Local/Docker to GDP Inverted Indexing
  - I have the communication between my GUI application and GCP implemented but for some reason only part of it works. My GUI application can run and has the buttons in which you click what data you want to run it on. It then sends the job and the job executes correctly on GCP. It also puts the output folder there with correct data (Again you can see all of this working in the demo video). The only thing it isnt able to do is move the result of the output back to the GU, Sadly it gets stuck trying to retrieve it in my code. For sure there is an exception but I was unable to solve this issue before submission.
  
My GUI application implementaion is in maven-example-jar/src/main/java/com/journaldev/maven/classes/MyGUI.java

My GUI was run using eclipse and compiled as a Maven project.
