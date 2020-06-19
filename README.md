# Kick-In26

## For Eclipse users: ##

1. Download our project into an empty folder from our git page: 
   https://git.snt.utwente.nl/di20-26/kick-in26

2. Open up Eclipse

3. Click on "File" -> "Import" -> "Existing Maven Project"

4. Click "Browse" and choose kickIn_26 inside the downloaded folder as Root Directory

5. Click "Finish"

6. Add kickInTeam26 to your TomCat server

7. Start the TomCat server

8. Go to URL: localhost:8080/kickInTeam26/login.html

9. Use the following credentials to log in
   - email: admin@gmail.com
   - password: adminpass

## If you are an IntelliJ user (great choice!) : ##

1. Download our project into an empty folder from our git page: 
   https://git.snt.utwente.nl/di20-26/kick-in26

2. Have Tomcat installed before opening the project.

3. Open up IntelliJ

4. Click "File" -> "Open", find the project file in the directory you downloaded and open the project.

5. After the project is open, right click on the pom.xml file and click on "+ Add as Maven Project".

6. On the right side of the screen, click on Maven and "Lifecycle" -> "install". 

7. Click Run -> Edit Configurations

8. Add a new configuration of type "Tomcat Server" -> "Local".

9. In the "Deployment" tab, add a new artifact with type ":war".

10. In the same tab, delete the "_war" from the "Application context" 

11. Make sure that the HTTP port is set to 8080 and hit "Apply" and "OK".

12. Run the Tomcat Server (it should automatically direct you to http://localhost:8080/kickInTeam26/login.html
after the artifact has been deployed successfully).

13. Use the following credentials to log in
  - email: admin@gmail.com
  - password: adminpass



