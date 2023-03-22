# COMP 361 Project

## Ruoyu's README is great

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

2. use `mvn clean javafx:run` to complie and run the code

#### Download The Pictures
Since we are not allowed to push the picture files into our repo, we have to download them somewhere locally.
1. Download `pictures.zip` in [this link](https://drive.google.com/drive/folders/1_qFamQnAU4fEEZqE0P-e6zrqeNkG2nRD)
2. Unzip it and put it under `client/src/main/resources/project`, and its location should be `client/src/main/resources/project/projects`
3. Now once you run `mvn clean javafx:run`, you can see the pictures showing up accurately in the app.

#### Other useful things
1. use `mvn clean package` to generate the java docs. (you can access the `html file` under `docs/project/`)



> Note: we can savely delete module-info.java if we are not developing a modular program.


## The Rules

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

 * ~~Maximilian Schiedermeier - [https://github.com/kartoffelquadrat]~~
 * ...


