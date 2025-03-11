# Contributing to CAB302-Project

Welcome to **CAB302-Project**! This project follows a straightforward Git workflow to keep things organized. Please read the following guidelines before contributing.

## Guidelines for Contributing

### 1. Branching

- **Create a new branch** for each feature you are working on. Name your branch something descriptive that reflects the feature or task (e.g., `feature/login-page` or `bugfix/fix-navbar`).
- **Do not work on the `main` branch**. It is reserved for stable, merged code. All development should happen in separate branches.

### 2. Working on One Feature at a Time

- **Work on only one feature per branch.** Once you finish one task and are ready to work on a new one, create a new branch for the new feature.
- If you are working on a feature and need to start a different one, make sure to **complete and submit your changes** for your current feature first before starting a new one.

### 3. Merging Changes

- Once your feature is complete and tested, **merge your branch into `main`** using the following command:
```bash
git merge --no-commit --no-ff <branch_name>
```
- The `--no-commit` flag prevents Git from automatically committing the merge, allowing you to review the changes before committing.
- The `--no-ff` flag ensures that a merge commit is created even if the merge could be fast-forwarded. This is important for maintaining a clear history of all merges.
- Provide a clear description of the changes in the merge commit. If you are unsure how to write a good commit message please read these resources [Better commit messages](https://www.freecodecamp.org/news/how-to-write-better-git-commit-messages/) and [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).
- Resolve any conflicts before merging. Here is a link to a YouTube video if you are unsure how resolve a [merge conflict](https://www.youtube.com/watch?v=Sqsz1-o7nXk)
- Ensure all tests pass before committing the merge.

### 4. Rebasing and Force Pushing
- Rebasing is strictly banned in this project. Always use merging for bringing changes into your branch or the main branch. Rebasing rewrites history and can cause issues for fellow collaborators.
- The use of git push --force is also banned. Force pushing can overwrite changes on remote branches, which can cause confusion and lost work for other collaborators. Always use git push without the --force flag.

### 5. Commit Message
- Write concise commit messages explaining WHY changes were made. Writing a good commit message allows others to understand your work and makes things easier later on when tracing back and debugging.
- The following format is recommended: `<type>(<scope>): <description>`. *(e.g.: fix(navbar): Change navbar alignment.)*
- Refer to this [link](https://www.conventionalcommits.org/en/v1.0.0/) on commit message best practices.