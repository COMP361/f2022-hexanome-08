# COMP 361 Project

## What is this project?
As a group of 7 people, we developed an online version of _Splendor_, a famous board game. The backend server was implemented using Spring Boot framework, 
and we used JavaFX as our frontend framework. 
The game is deployed on a [lobby service](https://github.com/m5c/LobbyService), developed by the course instructor: [Maximilian Schiedermeier](https://github.com/m5c).

## How to run everything by yourself (server and client)
### Prerequisites
1. Make sure you have [maven](https://maven.apache.org/download.cgi) installed on your machine. It is required to run
the client.
2. Download and install [docker desktop](https://www.docker.com/) so that you can run the server and database in some isolated containers.
3. Due to copy right issues, we can not upload the game assets of the original game in this repository, thus you must download it
from [here](https://drive.google.com/drive/folders/1_qFamQnAU4fEEZqE0P-e6zrqeNkG2nRD). Download the folder `pictures` and
store it somewhere on your machine. We will put it in the right place afterwards.

### Server
Let's start with the setting up the servers and database using `docker`:

#### docker compose up
Follow the steps by typing the command into your `terminal` (`cmd` if you are using windows)
1. git clone this [repository](https://github.com/RuoyuDeng/M8-Docker-BGP.git) by typing: `git clone https://github.com/RuoyuDeng/M8-Docker-BGP.git`
2. Then you cd into the directory: `cd M8-Docker-BGP`
3. Run the update submodules script
   1. For mac/linux, type: `./updatesubmodules.sh`
   2. For windows, you can first open the folder in explorer, and then right-click on the file `updatesubmodules.ps1`,
   in the prompt, click `run with powershell`.
4. After that, type: `docker compose up`
   > make sure you have docker desktop up and running before typing this
5. Wait for around 2-3 minutes (max), then you should have the `Lobby Server` and `Game Server` both running on your machine.
#### Alternative Approach after docker compose up (for developer)
After you have build the `docker images`, you can choose to run the `database` inside the container and the `Lobby Service` and `Game Server`
outside of the container. This approach is usually taken when you feel like customizing and modifying the game server or lobby server by yourself frequently 
during game play (not recommended for gamers).
1. Make sure you have the container `BGP-Database` created from previous `docker compose up` running. You can do so through the dashboard of the docker desktop application. 
2. `cd M8-Docker-BGP/LobbyService`, and then type: `mvn clean spring-boot:run`
3. Go back to the root folder: `M8-Docker-BGP`, then type: `cd f2022-hexanome-08/server`. Start the server by typing: `mvn clean spring-boot:run`
4. Despite several more steps, you have achieved the same goal as above approach.

Now, you should have the backend servers ready, let's see how to run the client.
### Client
We have not yet configured a way to package our frontend code to a `jar` file so that
it can be played cross-platform by simply double-clicking on one file. So the only way to
run the client (frontend) is use mvn: `mvn clean javafx:run`. Note that according to how the server is deployed, you might need to change the client
configuration a bit.
1. Remember the `pictures` folder that you downloaded as part of the prerequisite? Now let's put it in the right place, which is under:
   `M8-Docker-BGP/f2022-hexanome-08/client/src/main/resources/project/pictures`
2. Now depending on how you started your server and how exactly you want to play this game, there is some configuration details you need to do. If you want to:
   1. Play with your friends under a same WI-FI:
      1. Find the json configuration file: `M8-Docker-BGP/f2022-hexanome-08/client/connectionConfig.json`, the default content is:
      ```
      {
      "defaultUserName": "ruoyu", 
      "defaultPassword": "abc123_ABC123", 
      "useDefaultUserInfo": "true",
      "hostIp": "76.66.139.161",
      "useLocalHost": "false"
      }
      ```
   2. Change the value of `hostIp` to the IP of which your machine that runs the servers.
      > For mac user, you can do so by `ipconfig getifaddr en0` in your terminal. For example, you got 10.111.111.111 as your IP, then you need to change this configuration file in all the client applications: `"hostIp": "10.111.111.111"`,
   2. Play with your friends under different WI-FI:
      1. [Port-forwarding](https://www.hellotech.com/guide/for/how-to-port-forward#:~:text=To%20forward%20ports%20on%20your%20router%2C%20log%20into%20your%20router,you%20might%20have%20to%20upgrade.) your local host so that you can play the game with your friends under different WI-FI.
      2. Say the IP you got after port-forwarding is: `http://33.23.123.456/`, then similarly, you will need to replace `hostIp` to `"33.23.123.456"`, which is your own server IP after port-forwarding.
3. Now you have configured the client IP correctly, we are just one command line away from playing! Now type: `cd M8-Docker-BGP/f2022-hexanome-08/client` 
4. Lastly, type: `mvn clean javafx:run`, and you should be able to see the game running!

### I just want to play the game (client)
You could've skipped all the steps mentioned above if you just want to play some game ASAP. Follow the steps below:
1. Remember the `pictures` folder that you downloaded as part of the prerequisite? Now let's put it in the right place, which is under:
`M8-Docker-BGP/f2022-hexanome-08/client/src/main/resources/project/pictures`
2. Open your terminal or cmd, type: `cd M8-Docker-BGP/f2022-hexanome-08/client`
3. Then, type: `mvn clean javafx:run` to start the game!


### Lastly, Log-in Account Management
As you have noticed, we have provided you a default admin account to start the game with. For the very first time that you log in, you can use this account to add more accounts as you go.
Afterwards, you can change `"defaultUserName"` and `"defaultPassword"` to your own account information. The flag value: `"useDefaultUserInfo": "true"` enables that your password and username
being pre-filled for you (save some time) when you log in. If you do not like this feature, then you can simply change it to "false": `"useDefaultUserInfo": "false"`


**_Congrats! You have finished all the steps you needed to play the game (a bit long, I admit). Now it's time to gather some friends around, and start playing!_**

---
> Develop & Learning Logs
## Course Notes

### M7 prep
#### Backend
In order to keep a clean running environment, we will need the help from docker. To run the project correctly, follow the steps:
1. clone https://github.com/m5c/BoardGamePlatform any place you like.
2. cd into `BoardGamePlatform` and run `updatesubmodules.ps1` for windows with powershell or "./updatesubmodules.sh" for mac after you cloned it.
3. replace `BoardGamePlatform/LobbyService/ls-db-setup.sql` with `ls-db-setup.sql` in this link: https://drive.google.com/drive/folders/1D9_GvqYhzx_vdExBnbjYq9dK5b-d0Orf
4. make sure you are under `BoardGamePlatform` directory and docker desktop running, then type: `docker compose up` in the terminal, and you should have both database and lobby service ready.
5. use `mvn clean spring-boot:run -P test` under our `server` directory to run it, you should be good to go to test the server.
- Note: I have a dummy running instance of merging this manually running `server` thing into `docker compose up` prepared for M8, but for now, just follow these steps for M7.
6. Also run the LobbyService the same way


#### Frontend (how to run client)
1. Run the docker container of databased obtained from previous `docker compose up`. *(Only the database!!!!!)*
2. `mvn clean spring-boot:run` https://github.com/m5c/LobbyService.git the lobby service outside of the container
3. `mvn clean spring-boot:run -P test` under our server folder. The `-P test` argument is used to use a different property file to skip checkstyle/javadoc for testing purpose.
4. `mvn clean javafx:run` or click the run button in client folder. (I prefer the button)
5. You should be able to see a running instance of client.
** ipconfig getifaddr en0 to get ip address on mac


### M5 prep
1. Run code with a coverage report: the button on top right with a shield shape.

### M3 start-up template related

#### Compile and Run the template (mvn clean javafx:run)
1. `git pull` first to make sure your repo is up to date.
2. Then `cd client`, run `mvn clean javafx:run` to run the program.

#### Compile and Run the template (shaded jar file)
1. [Based on this instruction](https://www.youtube.com/watch?v=EyYb0GmtEX4&ab_channel=Randomcode), we can compile the code into a special jar file (shaded jar). Add it the following plugin to `<plugins></plugins>`:

```
<plugin>
               <groupId>org.apache.maven.plugins</groupId>
               <artifactId>maven-shade-plugin</artifactId>
               <version>3.2.4</version>
               <executions>
                   <execution>
                       <goals>
                           <goal>shade</goal>
                       </goals>
                       <configuration>
                           <shadedArtifactAttached>true</shadedArtifactAttached>
                           <transformers>
                               <transformer implementation=
                                                    "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                   <mainClass>com.game.hangman.Main</mainClass>
                               </transformer>
                           </transformers>
                       </configuration>
                   </execution>
               </executions>
           </plugin>
```
2. run `mvn clean package`, then `java -jar target/client-v1.0-shaded.jar` to start the application.

> Make sure you are in `client` directory before you run step 2.



#### Create a maven javafx project (For future reference)
1.[Follow the instruction](https://github.com/openjfx/javafx-maven-archetypes) to install all the archetypes in your local repository
Use the following code to create a new maven project with the `pom.xml` file 
(correct javafx-version)
```
mvn archetype:generate \
        -DarchetypeGroupId=org.openjfx \
        -DarchetypeArtifactId=javafx-archetype-fxml \
        -DarchetypeVersion=0.0.6 \
        -DgroupId=group08 \
        -DartifactId=client \
        -Dversion=version \
        -Djavafx-version=18.0.1
```

2. use `mvn clean javafx:run` to compile and run the code

#### Download The Pictures
Since we are not allowed to push the picture files into our repo, we have to download them somewhere locally.
1. Download `pictures.zip` in [this link](https://drive.google.com/drive/folders/1_qFamQnAU4fEEZqE0P-e6zrqeNkG2nRD)
2. Unzip it and put it under `client/src/main/resources/project`, and its location should be `client/src/main/resources/project/projects`
3. Now once you run `mvn clean javafx:run`, you can see the pictures showing up accurately in the app.

#### Other useful things
1. use `mvn clean package` to generate the java docs. (you can access the `html file` under `docs/project/`)


> Note: we can savely delete module-info.java if we are not developing a modular program.


## Course repo edit restrictions

* Feel free to edit/replace this file.
* Do not delete or rename the [reports](reports), [client](client), [server](server) or [docs](docs) directories.  
  See [Static Content](#static-content)
* Don't clutter your repo, update your [```.gitignore```](.gitignore) file, depending on your client language / technology.
   * Don't commit binaries. (Images, jar files, class files, etc...)
   * Don't commit buffer files. (Vim buffer files, IDE meta files etc...)
* Place your documentation in [```docs```](docs) on [master](branch).
* Commit frequently, commit fine grained.
* Use branches
* **Don't push on master!**
   * Create a new branch for your feature.
   * Work until stable / tested.
   * Merge / rebase your temporary branch back to master.
   * Delete your temporary branch.

## Static content

* [```.gitignore```](.gitignore): Preliminary git exclusion instructions. Visit [Toptal's generator](https://www.toptal.com/developers/gitignore) to update.
* [```reports```](reports): Base directory for automatic report collection. Your weekly reports go here. Must be uploaded every monday noon **to master** and follow correct date string ```YYYY-MM-DD.md```. Use [template](reports/YYYY-MM-DD.md) for your own reports. Do not resubmit same report / copy paste.
* [```docs```](docs): source directory for your [enabled GitHub Pages](https://comp361.github.io/f2022-hexanome-00/). (Update number in link). Configure IDE to generate API docs into this directory or build your own webpage (optional).
*  [```client```](client): Place your client sources into this directory. Do not use a separate repository for your work.
* [```server```](server): Place your Spring Boot Game Server sources in this directory. Do not use a separate repository for your work.

## Useful Links

### Code Style and Tools

* [Chrome MarkDown Plugin](https://chrome.google.com/webstore/detail/markdown-viewer/ckkdlimhmcjmikdlpkmbgfkaikojcbjk?hl=en).
   * Don't forget to enable ```file://``` in ```advanced settings```.
* [IntelliJ Checkstyle Plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).
   * Don't forget to enable [Google's Checkstyle Configuration](https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml).
* [Git CheatSheet](git-cheatsheet.md).
* [Advanced Rest Client (Rest Call Code Generator)](https://docs.advancedrestclient.com/installation).

### Requirements

* [Lobby Service](https://github.com/kartoffelquadrat/LobbyService)
   * [Install Guide](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/build-deploy.md)  
     Recommended: Startup in ```dev``` profile (default).
   * [API Doc and ARC Configurations](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/api.md)
   * [Game Developer Instructions](https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/game-dev.md)
* [BGP sample deployment configuration](https://github.com/kartoffelquadrat/BoardGamePlatform) (This one is meant for testing / understanding the interaction between LS, UI and sample game)  
  Board Game Platform (BGP) = Lobby Service + Lobby Service Web UI + Sample Game, all as docker containers.
   * Sample [Lobby Service Web UI](https://github.com/kartoffelquadrat/LobbyServiceWebInterface)
   * Sample Lobby Service compatible [Game (Tic Tac Toe, backend + frontend)](https://github.com/kartoffelquadrat/BgpXox)

> Be careful not to confuse *Lobby Service* and *Board Game Platform*.


## Authors

Fill e.g. names + link to github profiles in list below.

 * Maximilian Schiedermeier - [https://github.com/m5c]
 * [Ruoyu Deng](https://github.com/RuoyuDeng)
 * ....


