Android "example" playground or maybe event battlefield for testing and training with new libraries, programming patterns etc.

Application is currently about retrieving persons and relations from database, showing known people and creating groups of people that know each other. 

Screens (tablet version, phone version is the same, just without split screens, so only one fragment per screen):
https://github.com/JanuszHain/Android_Playground/tree/master/screenshots


Programming pattern:
- MVP (Dagger 2 with RxJava Usecases)

Libraries used:
- Dagger 2
- Retrofit (OkHTTP + GSON)
- Butter Knife
- RxJava
- RxAndroid
- Retrolambda
- Picasso

Licenses: 
https://github.com/JanuszHain/Android_Playground/blob/master/license.html

Future things that I want to test and implement:
- Automatic testing
- GreenDAO library



Installation:

Put this folder in htdocs (for example in xampp):
https://github.com/JanuszHain/AndroidPlayground/tree/master/installation_files/htdocs

Import MySQL database using phpmyadmin:
https://github.com/JanuszHain/AndroidPlayground/tree/master/installation_files/mysql_phpmyadmin_file

Change IP set in variable "webServiceUrl" in class NetModule for IP on which PHP will be hosted.
https://github.com/JanuszHain/Android_Playground/blob/master/app/src/main/java/pl/janusz/hain/androidplayground/data/internet/NetModule.java

After setup, after turning on XAMPP Apache and MySQL and connecting to same wifi, you should be able to connect to database and retrieve data from app!



About technical details:
MySQL is using stored procedures for preventing SQL Injections.
SQL commands are not using offset in queries with LIMIT. Instead of it "last id" is used to retreieve data.
Some columns are indexed for faster searching.

Android application is using RxJava for async work. It can work on few threads at once.
Applications for example loads relations for few people at the same time.

Also I optimised loading known persons.
https://github.com/JanuszHain/Android_Playground/blob/master/app/src/main/java/pl/janusz/hain/androidplayground/viewspresenters/persons/knownpersons/KnownPersonsPresenter.java
Relations are firstly loaded for later use for loading persons.
Next loads are "zipped" by RxJava. Persons to be shown are loaded concurrently to new relations for later use (for next persons load).
This way application doesn't need to wait for relations response before loading persons, so loading times are up to 2 times faster.

Looking for groups of mutual friends is also optimised by using RxJava's merge on several threads.
