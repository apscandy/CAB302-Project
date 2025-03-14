# CAB302-Project

Welcome to CAB302-Project! This is a university project developed using **Java 21**. Below you’ll find all the information you need to set up, run, and contribute to the project.


### Prerequisites

Ensure you have the following installed:

- [Java 21 Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
- [Git](https://git-scm.com/)
- [Intellij IDEA](https://www.jetbrains.com/idea/)
- [Maven (Optional)](https://maven.apache.org/download.cgi) The install process is bit more involved for windows users [here is a install guide](https://www.youtube.com/watch?v=Xatr8AZLOsE)

Additionally, ensure that you are part of the team on Trello and Discord. These tools are essential for team collaboration and project management.

## Setup

### Clone the repo
To get started with the project, clone this repository to your local machine using Git:

```bash
git clone https://github.com/apscandy/CAB302-Project.git
```
For user that have set up SSH keys

```bash
git clone git@github.com:apscandy/CAB302-Project.git
```

### Import the Project in IntelliJ
- Open IntelliJ IDEA.
- Select Open from the welcome screen.
- Navigate to the project folder you just cloned and open 

### Running the Project

#### Intellij
To run the project, follow these steps:

- Open the project in IntelliJ IDEA.
- In the Run menu, select Run 'Main' (or the class with your main method).
- The application should start running.

#### Terminal
If you have Maven installed you can use the CLI

After cloning the repo validate that the project is a valid Maven project with 
```sh
mvn validate
``` 
Running the code from the CLI with Maven installed
```sh
mvn javafx:run
```

Other useful command can be found [here](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html#:~:text=JEP%20247.-,Running%20Maven%20Tools,-Maven%20Phases)


## Contributing
Please take a look at the guidelines in the [CONTRIBUTING.md](./CONTRIBUTING.md) file before submitting any changes.

### Contributors
We would like to acknowledge all contributors to this project. A list of all contributors can be found in the [CONTRIBUTORS.md](./CONTRIBUTORS.md) file.


