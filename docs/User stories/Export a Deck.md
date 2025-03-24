created by: **Maverick Doan**

_As a user, I want to export decks so that I can share and back up my study materials._

**Acceptance Criteria:**

1. **Given** I have a deck, **when** I choose Export it, **then** a downloadable file should be generated in the selected format (CSV, JSON etc.)
2. **Given** I select the JSON format, **when** I export the deck, **then** I should receive a JSON file with the deck structured and generated file should include all flashcards with questions on one side and answers on the other.
3. **Given** I select the csv format, **when** I export the deck, **then** I should receive the generated file, which should include all flashcards with questions and answers organized into separate columns for easy readability.
4. **Given** the export fails due to an error (e.g.: file size too large), **when** I receive an error message, **then** I should be given an option to retry or split the deck.

**MoSCoW**: #Could-have 

**Priority:**  #Medium 

**Effort:**  
_6/10_

**Additional Notes:**  
None