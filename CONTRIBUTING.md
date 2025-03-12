# Contributing to CAB302-Project

Welcome to **CAB302-Project**! This project follows a straightforward Git workflow to keep things organized. Please read the following guidelines before contributing.

## Guidelines for Contributing

### 1. Branching

- **Create a new branch** for each feature you are working on. Name your branch something descriptive that reflects the feature or task (e.g., `feature/login-page` or `bugfix/fix-navbar`).
- **Do not work on the `main` branch**. It is reserved for stable, merged code. All development should happen in separate branches.

### 2. Working on One Feature at a Time

- **Work on only one feature per branch.** Once you finish one task and are ready to work on a new one, create a new branch for the new feature.
- If you are working on a feature and need to start a different one, make sure to **complete and submit your changes** for your current feature first before starting a new one.

### 3. Commit Messages
- **Purpose**: Write concise commit messages that clearly explain why the changes were made. Good commit messages help others understand your work and make it easier to trace and debug later.

- **Recommended Format**: The commit message should follow this format:
`<type>(<scope>): <description>`
For example: fix(navbar): adjust navbar alignment.

- **Capitalization and Punctuation**: Capitalize the first word, and avoid using punctuation at the end of the subject line. If using Conventional Commits, stick to lowercase.

- **Mood**: Use the imperative mood in the subject line (e.g., "Fix bug", "Add feature"). This gives your commit message a direct and action-oriented tone.

- **Type of Commit**: Specify the type of change made, such as "fix", "update", "refactor", or "bump". Consistency in these terms helps maintain clarity. For more on Conventional Commits, refer to the [Conventional Commits guidelines](https://www.conventionalcommits.org/en/v1.0.0/).

- **Length**: Keep the subject line under 50 characters, and limit the body of the message to 72 characters per line.

- **Content**: Be direct and eliminate unnecessary words like "maybe", "kind of", or "I think". Write like a journalist—clear and to the point.

### 4. Merging Changes

- Once your feature is complete and tested, **merge your branch into `main`** using the following command:
```bash
git merge --no-commit --no-ff <branch_name>
```
- The `--no-commit` flag prevents Git from automatically committing the merge, allowing you to review the changes before committing.
- The `--no-ff` flag ensures that a merge commit is created even if the merge could be fast-forwarded. This is important for maintaining a clear history of all merges.
- **Peer Review**: Before merging your changes, it is considered good practice to have someone review your feature branch. This helps catch potential issues and ensures the code aligns with project standards.
- **Merge Process (Optional)**: Although not required, it’s a good practice to have someone else, typically a more senior or more experienced team member, run the actual merge into the main branch. This can help ensure the merge is done correctly.
- Resolve any conflicts before merging. Here is a link to a YouTube video if you are unsure how resolve a [merge conflict](https://www.youtube.com/watch?v=Sqs)
- Ensure all tests pass before committing the merge.

### 5. Rebasing and Force Pushing
- Rebasing is strictly banned in this project. Always use merging for bringing changes into your branch or the main branch. Rebasing rewrites history and can cause issues for fellow collaborators.
- The use of git push --force is also banned. Force pushing can overwrite changes on remote branches, which can cause confusion and lost work for other collaborators. Always use git push without the --force flag.
