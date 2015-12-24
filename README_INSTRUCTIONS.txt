--------------------------------------------------------------------
Configuring IntelliJ IDE for jade, spark and jackson using a maven template:
--------------------------------------------------------------------

1.Create a maven project and in the "pom.xml" file paste the following code inside the tag "<project>":

<repositories>
	<repository>
		<id>tilab</id>
		<url>http://jade.tilab.com/maven/</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>com.sparkjava</groupId>
		<artifactId>spark-core</artifactId>
		<version>2.2</version>
	</dependency>

	<dependency>
		<groupId>com.tilab.jade</groupId>
		<artifactId>jade</artifactId>
		<version>4.3.3</version>
	</dependency>

	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-core</artifactId>
		<version>2.5.3</version>
	</dependency>

	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-annotations</artifactId>
		<version>2.5.3</version>
	</dependency>

	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-databind</artifactId>
		<version>2.5.3</version>
	</dependency>
</dependencies>

This will import jade, spark and jackson.
Spark will be used to create the java web server, jackson for a json parser and jade for the multi-agent system arquitecture.

2.Import the packages to the "src->main" folder

--------------------------------------------------------------------
Configuring the webserver to show our page
--------------------------------------------------------------------
1.Install a wamp server (xamp is equivalent, or lamp for linux)

If using wamp:
2.Go to folder "C:\wamp\bin\apache\apache2.4.9\conf" and paste the provided "httpd.conf" file

3.Open the "extra" folder on the same directory and edit "httpd-vhosts.conf" to find your page like this:

<VirtualHost *:80>
   ServerName localhost
   DocumentRoot C:/wamp/www

   <Directory C:/wamp/www>
       AllowOverride All
       Require all granted
   </Directory>
</VirtualHost>

<VirtualHost *:80>
   ServerName sensores.dev
   DocumentRoot C:/wamp/www/sensores/public

   <Directory C:/wamp/www/sensores/public>
       AllowOverride All
       Require all granted
   </Directory>
</VirtualHost>

This will search for a folder "sensores" inside "C:/wamp/www/" where the files for the webpage will have to be.

4.Edit "C:\Windows\System32\drivers\etc\hosts" with administrator rights and paste the following line "127.0.0.1       localhost sensores.dev"

"sensores.dev" is the name we chose, it can be any other name as long as the respective virtual host "code" as the same "ServerName" property.

5.paste the given "sensores" folder at "C:/wamp/www/"

With all this, and if the wamp desktray icon is green after restarting all services (left click on the desktray icon -> "Restart All Services"), the webpage should be working already. 

--------------------------------------------------------------------
Running the whole thing
--------------------------------------------------------------------

1.run startJade.bat from folder and wait for the program to startJade

2.run Main.java file in IntelliJ

3.open a web browser (Google Chrome recommended) and allow the page to locate the computer (its safe because its not really online now)

4.from this point the GUI is pretty simple and straighforward to work with

