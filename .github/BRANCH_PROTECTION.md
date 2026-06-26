# Branch Protection

Recommended settings for `main` after the first green CI run:

- Require pull requests before merging.
- Require status checks to pass: `ci`.
- Require branches to be up to date before merging.
- Require signed commits if the organization policy already uses them.
- Restrict force pushes and deletions.
