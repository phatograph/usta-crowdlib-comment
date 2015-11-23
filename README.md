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

## Requirements

##### A Comment is posted by a User and can either be a new comment on a library catalogue item or a reply to a previous comment.

Comments can be added to an item.

``` bash
$ curl -H "Content-Type: application/json" -X POST -d '{"content":"New comment!"}' http://localhost:9998/items/0/reply -v -s
```

Or also to another comment.

``` bash
$ curl -H "Content-Type: application/json" -X POST -d '{"content":"Another new comment!"}' http://localhost:9998/comments/604/reply -v -s
```

##### Comments are timestamped.

Posting a comment will introduce the current timestamp.

``` bash
$ curl -H "Content-Type: application/json" -X POST -d '{"content":"New comment!"}' http://localhost:9998/items/0/reply -v -s
{
  "date": "Nov 23, 2015 5:35:37 PM",
  "itemId": 0,
  "favourites": 0,
  "id": 604,
  "user": {
    "id": 0,
    "name": "Phat",
    "surname": "Wangrungarun",
    "isAdmin": true
  },
  "content": "New comment!",
  "parentId": -1
}
```

##### For any given library catalogue item, it should be possible to obtain the comments
posted. A paging feature should allow users to select 20, 50 or 100 comments at a
time.

To get the paging to work, one needs to specify `limit` and/or `from` query strings.
For exmaple, consider this following request.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/items/1\?limit\=2\&from\=3 -v -s
```

This means you're getting an information of comment id `1`, with its 2 comments,
starting from the 3rd one.

##### Users should be able to select a comment as a favourite and should be able to
retrieve all the comments the have put on their list of favourites.

To favourite a comment.

``` bash
$ curl -H "Content-Type: application/json" -X POST http://localhost:9998/comments/1/favourite -v -s
```

To get a user favoured comments.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/users/0/favourites -v -s
```

##### For any comments it should be possible to retrieve the replies to this comment
including replies to replies and so on.

##### Users should be able to follow a bibliographic item’s comments. Any new comments
posted on a bibliographic item will show up in a list of notifications.

##### Once seen by a user, a comment should be removed from the list of notifications.

##### Each comment should display a count of how many users have put it on their lists of
favourites.

##### A user should be able to remove a comment they posted from view. This means that
its content should be replaced with an explanatory message saying that the post was
removed by the user

##### A user in the admin role can remove comments from view. They are flagged in the
system as being removed by a moderator and the content is no longer provided to
clients. An explanatory note is provided instead of the original content.

##### For the purposes of this assignment, an in-memory representation is sufficient (but
note the extensions section below).

##### Similarly, authentication can be through a simple static username/password list.

##### The RESTful API and client code must make use of Jersey, the reference
implementation of the JAX-RS standard.

##### The API should follow the principles of RESTful design (cf. Bill Burke’s RESTful Java
with JAX-RS, Chapter 1).

##### Unit testing can be written either as plain JUnit tests or using the Spock framework.
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

## CORS
