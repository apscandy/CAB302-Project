created by: **David Bui**
**Reason for updating**: To avoid using external services

_As a user who forgot my password, I want to verify my identity through my registered email so that I can securely reset my password and regain access to my account._

**Acceptance Criteria:**

1. **Given** a user is on the login page, **when** they click "Forgot Password," **then** they are directed to the password recovery screen.
2. **Given** a user is on the password recovery page, **when** they enter their registered username or email, **then** they receive an email containing a secure password reset link.
3. **Given** a user receives the password reset email, **when** they click on the reset link, **then** they are directed to a secure page where they can create a new password.
4. **Given** a user clicks on an expired or invalid reset link, **when** they attempt to use it, **then** they receive an error message prompting them to request a new link.
5. **Given** a user is setting a new password, **when** they enter a password that meets security requirements, **then** their password is updated successfully.

**Priority:**
_Medium_

**Effort:**
_5/10_

**Additional Notes:**

- During registration, users must provide a valid email address.
- Provide an option in the user profile for updating the registered email address.