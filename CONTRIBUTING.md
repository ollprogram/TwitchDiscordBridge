# Contributing

You are welcome to contribute to this project.
If you wish to do so, you can fork the repository, create a branch named after your feature, and open a Pull Request.
If you are unsure or want to discuss an idea first, feel free to create a new [issue](README.md#report-a-bug-something-to-improve-or-ask-for-help)

## Commit naming conventions

Commits must follow this format:
```
type(topic) title 
description
```

where type can be : 
- `refactor` : for refactoring
- `feat` : when adding a new feature
- `fix` : for fixes
- `docs` : when updating the documentation, comment, etc...
- `style` : for changes that do not affect application behavior (e.g., modifying a CLI message)
- `test` : when adding or updating tests
- `security` : for security-related fixes

The topic can be any single word that describes which part of the application you modified (e.g., cli, config, bridge, project, readme…)

Example : 

```
feat(commands) added the stat command
I added new command "stat" to get all usage statistics of the bot
```

> Your title can also be a short description

## Branches conventions

Branch names must follow this format :

`type/title`

> type is the same as for [commits](#commit-naming-conventions)

Example :

`feat/stat_command`

## Code conventions :

- Use standard Java/Go naming conventions.
- `@Notnull` annotation is mandatory on public methods to avoid error caused by incorrect usage. `@Notnull` is considered more important than `@Nullable` because it automatically generates null-checking code.
- Consider the usage of Optional instead of nullable results or parameters.
- Treat values as nullable by default. If something public is not annotated with `@NotNull`, assume it could be nullable.
- Add unit tests for every feature added if possible.
- Update unit tests if necessary.
- Every public class or method must be documented with JavaDoc.
- Update the documentation if necessary (javadoc, readme, contributing).
- Code must not contain any lint warnings for PRs or releases. If a warning cannot be avoided, it must be explicitly justified in a comment.
- All unit tests must pass.
- The code must compile without any errors.

## Dependencies

This project use three APIs :

- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)
- [Twitch4J (Java Twitch API)](https://github.com/twitch4j/twitch4j)

  You can see the Maven pom.xml [here](https://github.com/ollprogram/TwitchDiscordBridge/blob/main/pom.xml).


## Dev container

To use the dev container, you need [Docker Desktop](https://www.docker.com/products/docker-desktop/) and [VSCode](https://code.visualstudio.com/), running the [devcontainer extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers).

Once these dependencies are installed, you can open the project in a new container from VSCode, the project and all of its dependencies will be imported automatically.

On container startup, Maven will clean, validate, compile and test the project.
