# Contributing

## Dependencies

For this project I'm using three APIs :

- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)
- [Twitch4J (Java Twitch API)](https://github.com/twitch4j/twitch4j)

  You can see the Maven pom.xml [here](https://github.com/ollprogram/TwitchDiscordBridge/blob/main/pom.xml).


## Devcontainer

To use the devcontainer, you need [Docker Desktop](https://www.docker.com/products/docker-desktop/) and [VSCode](https://code.visualstudio.com/), running the [devcontainer extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers).

Once these dependencies are installed, you can open the project in a new container from VSCode, the project and all of its dependencies will be imported automatically.

On container startup, Maven will clean, validate, compile and test the project.

## JavaDocs