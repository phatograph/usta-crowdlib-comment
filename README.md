## Introduction

To run the unit test, cobertura code coverage and checkstyle tasks use the 
following commands on the command line ($ denotes the command prompt) or their
equivalents in Eclipse.

Tests:

``` bash
gradle test
open build/reports/tests/index.html
```

Cobertura:

``` bash
gradle cobertura
open build/reports/cobertura/index.html
```

Checkstyle:

``` bash
gradle check
open build/reports/checkstyle/main.html
```

## API

> 1. A Comment is posted by a User and can either be a new comment on a library catalogue item or a reply to a previous comment.

> 2. Comments are timestamped.

> 3. For any given library catalogue item, it should be possible to obtain the comments
posted. A paging feature should allow users to select 20, 50 or 100 comments at a
time.

> 4. Users should be able to select a comment as a favourite and should be able to
retrieve all the comments the have put on their list of favourites.

> 5. For any comments it should be possible to retrieve the replies to this comment
including replies to replies and so on.

> 6. Users should be able to follow a bibliographic item’s comments. Any new comments
posted on a bibliographic item will show up in a list of notifications.

> 7. Once seen by a user, a comment should be removed from the list of notifications.

> 8. Each comment should display a count of how many users have put it on their lists of
favourites.

> 9. A user should be able to remove a comment they posted from view. This means that
its content should be replaced with an explanatory message saying that the post was
removed by the user

> 10. A user in the admin role can remove comments from view. They are flagged in the
system as being removed by a moderator and the content is no longer provided to
clients. An explanatory note is provided instead of the original content.

> 11. For the purposes of this assignment, an in-memory representation is sufficient (but
note the extensions section below).

> 12. Similarly, authentication can be through a simple static username/password list.

> 13. The RESTful API and client code must make use of Jersey, the reference
implementation of the JAX-RS standard.

> 14. The API should follow the principles of RESTful design (cf. Bill Burke’s RESTful Java
with JAX-RS, Chapter 1).

> 15. Unit testing can be written either as plain JUnit tests or using the Spock framework.
Unit tests should involve mock objects where appropriate.

### Change current user

``` bash
curl -v -H "Content-Type: application/json" -X PUT http://localhost:9998/users/{user_id}/act  
```

### Get notifications

``` bash
curl -v -H "Content-Type: application/json" http://localhost:9998/users/notifications
```

### Get followings

``` bash
curl -v -H "Content-Type: application/json" http://localhost:9998/users/followings
```

### Get user information

``` bash
curl -v -H "Content-Type: application/json" http://localhost:9998/users/{user_id}
```

### Create a new item

``` bash
curl -H "Content-Type: application/json" -X POST -d '{"content":"Turkish goes to the Moon"}' http://localhost:9998/items
```
