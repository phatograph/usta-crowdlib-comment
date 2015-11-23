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

#### A Comment is posted by a User and can either be a new comment on a library catalogue item or a reply to a previous comment.

Comments can be added to an item.

``` bash
$ curl -H "Content-Type: application/json" -X POST -d '{"content":"New comment!"}' http://localhost:9998/items/0/reply -v -s
```

Or also to another comment.

``` bash
$ curl -H "Content-Type: application/json" -X POST -d '{"content":"Another new comment!"}' http://localhost:9998/comments/604/reply -v -s
```

#### Comments are timestamped.

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

#### For any given library catalogue item, it should be possible to obtain the comments posted. A paging feature should allow users to select 20, 50 or 100 comments at a time.

To get the paging to work, one needs to specify `limit` and/or `from` query strings.
For exmaple, consider this following request.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/items/1\?limit\=2\&from\=3 -v -s
```

This means you're getting an information of comment id `1`, with its 2 comments,
starting from the 3rd one.

#### Users should be able to select a comment as a favourite and should be able to retrieve all the comments the have put on their list of favourites.

To favourite a comment.

``` bash
$ curl -H "Content-Type: application/json" -X POST http://localhost:9998/comments/1/favourite -v -s
```

To get a user favoured comments.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/users/0/favourites -v -s
```

#### For any comments it should be possible to retrieve the replies to this comment including replies to replies and so on.

To retrieve nested comments, do so as in the following which retrieves nested comments
of comment id `1`.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/comments/1 -v -s 
```

#### Users should be able to follow a bibliographic item’s comments. Any new comments posted on a bibliographic item will show up in a list of notifications.

To follow an item, do so, as in the following which follows
item id `1`.

``` bash
$ curl -H "Content-Type: application/json" -X POST http://localhost:9998/items/1/follow -v -s
```

Which following items can be retrieved as follow.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/users/followings -v -s 
[
  ...,
  {
    "id": 2,
    "user": {
      "id": 0,
      "name": "Phat",
      "surname": "Wangrungarun",
      "isAdmin": true
    },
    "item": {
      "id": 1,
      "title": "Last Fantasy",
      "user": {
        "id": 1,
        "name": "Sebastian",
        "surname": "Duque",
        "isAdmin": false
      }
    }
  }
]
```

In order to test the notification, you need to change your current
active user to another user. Because if you comment on the item
yourself you won't get a notification.

``` bash
$ curl -H "Content-Type: application/json" -X PUT http://localhost:9998/users/1/act -v -s 
```

And then make a comment.

```
$ curl -H "Content-Type: application/json" -X POST -d '{"content":"Another new comment!"}' http://localhost:9998/items/1/reply -v -s 
```

Then change the active user back.

``` bash
$ curl -H "Content-Type: application/json" -X PUT http://localhost:9998/users/0/act -v -s 
```

Then check for a new notification.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/users/notifications -s | jq '.'  
[
  {
    "id": 604,
    "user": {
      "id": 0,
      "name": "Phat",
      "surname": "Wangrungarun",
      "isAdmin": true
    },
    "comment": {
      "id": 605,
      "content": "Another new comment!",
      "user": {
        "id": 1,
        "name": "Sebastian",
        "surname": "Duque",
        "isAdmin": false
      },
      "item": {
        "id": 1,
        "title": "Last Fantasy",
        "user": {
          "id": 1,
          "name": "Sebastian",
          "surname": "Duque",
          "isAdmin": false
        }
      },
      "date": "Nov 23, 2015 6:18:55 PM",
      "status": 0
    },
    "isRead": false
  }
]
```



#### Once seen by a user, a comment should be removed from the list of notifications.

TBD

#### Each comment should display a count of how many users have put it on their lists of favourites.

TBD

#### A user should be able to remove a comment they posted from view. This means that its content should be replaced with an explanatory message saying that the post was removed by the user

TBD

#### A user in the admin role can remove comments from view. They are flagged in the system as being removed by a moderator and the content is no longer provided to clients. An explanatory note is provided instead of the original content.

TBD

## API

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
