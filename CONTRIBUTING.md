_Last updated: 12/04/2025_

# 🚀 Team Git Guidelines — Shared `dev` Branch (No PRs)

Hey Team!  
Since we all work directly on the shared `dev` branch, it’s super important that we follow these simple habits to avoid overwriting each other’s work or introducing bugs.

---

## 🌳 Branching Strategy

- `main`: Stable, production-ready code. No direct pushes allowed.
- `dev`: Shared branch for all development.
- **Optional but Recommended:** Use temporary **feature branches** for your work

---

## 💡 Your Daily Git Flow

1. **Always Pull First**  
   Before you start any work, make sure your local `dev` is up to date:
   ```bash
   git checkout dev
   git pull origin dev
   ```

2. **Create a Feature Branch**
    - When you start a new feature or fix, create a new branch.
    - Only one person should work on each branch — no sharing branches.
    - Use screen sharing if collaborating, not the same branch.
   ```bash
   git checkout -b feature/your-task-name
   ```

3. **Commit Locally as You Work**  
   Small, clear, descriptive commits are easier to track:
   ```bash
   git add .
   git commit -m "Describe your change"
   ```

4. **Rebase `dev` Before Pushing**  
   Sync your work with the latest `dev` so you don’t overwrite someone else’s work:
   ```bash
   git checkout dev
   git pull origin dev
   git checkout feature/your-task-name
   git rebase dev
   ```

5. **Push to `dev`**
    - Once your work is complete and tested locally, you must get approval from another team member. Post a message in the Discord dev-chat with a short description of what you’ve done and ask for a quick review.
    - Only merge once someone gives a 👍 or confirms it looks good.


6. **Merge Your Feature into `dev`**
   After getting approval
    ```bash
    git checkout dev
    git merge feature/your-task-name
    git push origin dev
    ```

7. **Delete the Feature Branch**  
   After your code is merged into `dev`:
    - **Do not reuse the same branch** for future work.
    - **Delete the branch locally and remotely to avoid confusion**.
   ```bash
   git branch -d feature/your-task-name      # delete local
   git push origin --delete feature/your-task-name  # delete remote
   ```


---

## ⚡ Golden Rules

- **Pull before you push**: Always pull the latest version of `dev` before merging your changes and pushing.
- **Don’t overwrite work**: If Git warns about conflicts — pause and solve them locally before pushing.
- **One branch = one person**: Never share a feature branch.
- **Don’t reuse old branches**: Once merged, the branch is done — start fresh for each new task.
- **No rebasing on `dev` or `main`**: Rebasing can mess up the shared history.
- **No force pushing (`git push --force`)**: This can overwrite other people’s work.
- **Commit messages should be short and clear**
   ```
   Add login form
   Fix navbar bug
   Improve loading animation
   ```
- **Test locally and get approval before merging into dev.**

---

## 🧑‍🏫 Helpful Commands Cheat Sheet

| Purpose                  | Command                                                    |
|--------------------------|------------------------------------------------------------|
| Clone the repo https     | `git clone https://github.com/apscandy/CAB302-Project.git` |
| Clone the repo ssh       | `git clone git@github.com:apscandy/CAB302-Project.git`     |
| Switch to `dev`          | `git checkout dev`                                         |
| Pull the latest `dev`    | `git pull origin dev`                                      |
| Create feature branch    | `git checkout -b feature/your-task-name`                   |
| Add changes              | `git add .`                                                |
| Commit changes           | `git commit -m "Your message"`                             |
| Merge feature into `dev` | `git checkout dev && git merge feature/your-task-name`     |
| Push `dev`               | `git push origin dev`                                      |

---

## 🔥 Final Word

No PRs means **trust and teamwork** are key.  
When in doubt: **Pull. Test. Then Push.**  
And always let the team know when you're pushing something important!

