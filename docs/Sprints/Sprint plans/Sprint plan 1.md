 

## Sprint Details
**Sprint Duration:** 02/04/25 - 16/04/25  
**Sprint Goal:** Project setup - getting user authenticationService done and the main view configured  

## Team Members
- [[Andrew Clarke]]
- [[Monica Borg]]
- [[Minh Son Doan (Maverick)]]
- [[Dang Linh Phan (Lewis)]]
- [[Hoang Dat Bui (David)]]
## Sprint Backlog
| User Story             | Priority           | Assignee | Status       |
| ---------------------- | ------------------ | -------- | ------------ |
| [[User Login]]         | #Must-have / #High | TBA      | #in-progress |
| [[User Log Out]]       | #Must-have / #High | TBA      | #in-progress |
| [[User Registration]]  | #Must-have / #High | TBA      | #in-progress |
| [[Security Questions]] | #Must-have / #High | TBA      | #in-progress |
| [[Create a Deck]]      | #Must-have / #High | TBA      | #in-progress |

## Tasks Breakdown
| Task                                                                                                                                   | Related User Story                                                                  | Assignee                                                                          | Status |
| -------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- | ------ |
| Create `User.java`, `IUserDAO` and `SqliteUserDAO`                                                                                     | [[User Login]], [[User Log Out]], [[User Registration]]  and [[Security Questions]] | [[Minh Son Doan (Maverick)]] [[Hoang Dat Bui (David)]] [[Dang Linh Phan (Lewis)]] |        |
| Create `LoginController`, `RegisterController` and `SecurityQuestionController` and views associated with the user story and UI design | [[User Login]], [[User Log Out]], [[User Registration]]  and [[Security Questions]] | [[Minh Son Doan (Maverick)]] [[Hoang Dat Bui (David)]] [[Dang Linh Phan (Lewis)]] |        |
| Create `Deck.java`, `IDeckDAO` and `SqlitleDeckDAO`                                                                                    | [[Create a Deck]] [[Delete a Deck]]                                                 | [[Andrew Clarke]]                                                                 |        |
| Create `DeckController` and and all view associated with the user story and UI design                                                  | [[Create a Deck]] [[Delete a Deck]]                                                 | [[Andrew Clarke]]                                                                 |        |
| Create `Card.java`, `ICardDAO` and `SqliteCardDAO`                                                                                     | [[Add Flashcard to a Deck]] [[Edit a Flashcard]] [[Delete a Flashcard]]             | [[Monica Borg]]                                                                   |        |
| Create `CardController` and views associated with the user story and UI design                                                         | [[Add Flashcard to a Deck]] [[Edit a Flashcard]] [[Delete a Flashcard]]             |                                                                                   |        |

 

## Sprint Meetings 
**Weekly Standup:** 09/04/25 
**Sprint Review:** 14/04/25
**Sprint Retrospective:** 17/04/25 

## Risks & Dependencies
-  Database configurations for the user need to be setup first.
-  Team is still adjusting to Git in the context of teamwork, there is a possibility to lose information in a Git merge if there is a conflict.

## Definition of Done (DoD)
- [ ] Code reviewed and merged
- [ ] Documentation updated
- [ ] Feature demoed & approved

## Notes & Additional Comments