
## Sprint Details
**Sprint Number:** 2
**Sprint Duration:** 16/04/2025 - 22/04/2025  
**Sprint Goal:** Complete the next stage of the application for remaining functionality bar test modes. 

## Team Members
- Andrew Clarke  
- Monica Borg  
- Hoang Dat Bui (David)
- Minh Son Doan (Maverick)
- Dang Linh Phan (Lewis)

## Sprint Backlog
| User Story | Priority | Assignee | Status |
|------------|----------|----------|---------|
| Import Deck | Med | Maverick | #To-Do |
| Export Deck | Med | Maverick | #To-Do |
| Profile Management | Low | Lewis | #To-Do |
| Undo Changes (aka Recycle Bin) | Low | David | #To-Do |
| View Bookmarked Decks | Low | Monica | #To-Do |
| Bookmark a Deck | Low | Monica | #To-Do |
| View Analytics | High | Andrew | #To-Do |

## Tasks Breakdown
| Task | Related User Story | Assignee | Status |
|------|--------------------|----------|---------|
| Design and implement CSV parser utility | [[Import Deck]], [[Export Deck]] | [[Maverick]] | #To-Do |
| Create `ImportExportController` and associated views | [[Import Deck]], [[Export Deck]] | [[Maverick]] | #To-Do |
| Create UI flow for uploading and validating import files | [[Import Deck]] | [[Maverick]] | #To-Do |
| Implement download/export functionality with file generation logic | [[Export Deck]] | [[Maverick)]] | #To-Do |
| Create `ProfileController` and UI for editing profile fields | [[Profile Management]] | [[Lewis]] | #To-Do |
| Update `User.java` and DAO to support profile edit actions | [[Profile Management]] | [[Lewis]] | #To-Do |
| Implement soft delete recovery mechanism with deck–card dependency handling | [[Undo Changes (aka Recycle Bin)]] | [[David]] | #To-Do |
| Design UI for recycle bin to show deleted items and restore options | [[Undo Changes (aka Recycle Bin)]] | [[David]] | #To-Do |
| Extend `Card.java` and DAO to support `isBookmarked` flag | [[Bookmark a Deck]], [[View Bookmarked Decks]] | [[Monica]] | #To-Do |
| Create UI button/indicator for bookmarking/unbookmarking in flashcard viewer | [[Bookmark a Deck]] | [[Monica]] | #To-Do |
| Create a `BookmarkController` and dedicated view to list bookmarked flashcards | [[View Bookmarked Decks]] | [[Monica]] | #To-Do |
| Create a charting utility for visualisation | [[View Test Results]] | [[Andrew]] | #To-Do |
| Implement chart display logic based on correct/incorrect history per deck | [[View Test Results]] | [[Andrew]] | #To-Do |
| Connect analytics view to stored results in DB | [[View Test Results]] | [[Andrew]] | #To-Do |


## Sprint Meetings
**Weekly:** <!-- Time & Recurring Schedule -->  
**Sprint Review:** 20/04/2025 
**Sprint Retrospective:** 23/04/2025  

## Risks & Dependencies
- File parsing (CSV) may fail if formatting varies – validation and error handling are essential.  
- Import preview requires safe temporary storage – users cancelling midway should not commit partial data.  
- External library usage for charts or file I/O may create integration or deployment challenges.  
- File operations (import/export) need to clearly handle failure cases (e.g., oversized file, wrong format).  
- Undo functionality depends on parent-child data relationships – cards cannot be restored if their parent deck is permanently deleted. Further, there should be a function within the recycle bin to clear completely (hard delete). 
- Bookmarking depends on accurate card identifiers – any changes to how cards are referenced could impact logic.  
- Charts depend on consistent performance data logging – missing/incomplete data may cause broken visualisation.

## Definition of Done (DoD)
- [ ] Code reviewed and merged
- [ ] Unit & integration tests passed
- [ ] Documentation updated
- [ ] Feature demoed & approved

## Notes & Additional Comments
- All team members will work on new branches per their task (i.e. feat/import-export or feat/recycle-bin).
- TEST YOUR WORK BEFORE PUSHING/MERGING.
- As the user set up is relatively complete, we should all be able to work independently. No stories this sprint should effect another. 
