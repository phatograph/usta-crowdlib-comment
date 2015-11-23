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

A user can mark all notification as read as follow.

``` bash
$ curl -H "Content-Type: application/json" -X PUT http://localhost:9998/users/notifications -s
```

Re-check the notification.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/users/notifications -s | jq '.'
[]
```

#### Each comment should display a count of how many users have put it on their lists of favourites.

Once you retrieve a comment, either top-level or nested,
favourites count will also be presented. The next example
is a top-level comment.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/items/0 -s | jq '.'
{
  "item": {
    "id": 0,
    "title": "Lord of the Rings",
    "user": {
      "id": 0,
      "name": "Phat",
      "surname": "Wangrungarun",
      "isAdmin": true
    }
  },
  "comments": [
    {
      "date": "Nov 23, 2015 5:52:44 PM",
      "itemId": 0,
      "favourites": 1,
      "id": 0,
      "user": {
        "id": 0,
        "name": "Phat",
        "surname": "Wangrungarun",
        "isAdmin": true
      },
      "content": "Outstanding.",
      "parentId": -1
    },
    ...
  ],
  ...
}
```

Another example for nested comments (from comment id `0`).

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/comments/0 -s | jq '.'
[
  {
    "date": "Nov 23, 2015 5:52:44 PM",
    "itemId": 0,
    "favourites": 0,
    "id": 2,
    "user": {
      "id": 1,
      "name": "Sebastian",
      "surname": "Duque",
      "isAdmin": false
    },
    "content": "Indeed.",
    "parentId": 0
  },
  {
    "date": "Nov 23, 2015 5:52:44 PM",
    "itemId": 0,
    "favourites": 0,
    "id": 3,
    "user": {
      "id": 2,
      "name": "Suhyun",
      "surname": "Cha",
      "isAdmin": false
    },
    "content": "Totally agreed.",
    "parentId": 0
  }
]
```

#### A user should be able to remove a comment they posted from view. This means that its content should be replaced with an explanatory message saying that the post was removed by the user

User can remove a comment as follow.

``` bash
$ curl -H "Content-Type: application/json" -X DELETE http://localhost:9998/comments/0 -s
```

It will be displayed with a different message, but the actual content is
still kept.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/items/0 -s | jq '.'
{
  "item": {
    "id": 0,
    "title": "Lord of the Rings",
    "user": {
      "id": 0,
      "name": "Phat",
      "surname": "Wangrungarun",
      "isAdmin": true
    }
  },
  "comments": [
    {
      "date": "Nov 23, 2015 5:52:44 PM",
      "itemId": 0,
      "favourites": 1,
      "id": 0,
      "user": {
        "id": 0,
        "name": "Phat",
        "surname": "Wangrungarun",
        "isAdmin": true
      },
      "content": "The post was removed by the user.",
      "parentId": -1
    },
    ...
  ],
  ...
}
```

User can also restore their comment as follow.

``` bash
$ curl -H "Content-Type: application/json" -X PUT http://localhost:9998/comments/0/restore -s 
```

Moreover, a non-admon user cannot remove other's comments,
which will result in 403 Forbidden.

``` bash
$ curl -H "Content-Type: application/json" -X PUT http://localhost:9998/users/1/act -v -s

$ curl -H "Content-Type: application/json" -X DELETE http://localhost:9998/comments/0 -s -v | jq '.'
*   Trying ::1...
* connect to ::1 port 9998 failed: Connection refused
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 9998 (#0)
> DELETE /comments/0 HTTP/1.1
> Host: localhost:9998
> User-Agent: curl/7.43.0
> Accept: */*
> Content-Type: application/json
> 
< HTTP/1.1 403 Forbidden
< Access-Control-Allow-Origin: *
< Access-Control-Allow-Methods: GET, POST
< Access-Control-Allow-Headers: X-Requested-With, Content-Type
< Date: Mon, 23 Nov 2015 18:39:04 GMT
< Content-Length: 0
< 
* Connection #0 to host localhost left intact
```

#### A user in the admin role can remove comments from view. They are flagged in the system as being removed by a moderator and the content is no longer provided to clients. An explanatory note is provided instead of the original content.

An admin uses the same request to remove other's comment.

``` bash
$ curl -H "Content-Type: application/json" -X DELETE http://localhost:9998/comments/1 -s
```

Which would result in a moderator message.

``` bash
$ curl -H "Content-Type: application/json" http://localhost:9998/items/0 -s | jq '.'
{
  "item": {
    "id": 0,
    "title": "Lord of the Rings",
    "user": {
      "id": 0,
      "name": "Phat",
      "surname": "Wangrungarun",
      "isAdmin": true
    }
  },
  "comments": [
    ...,
    {
      "date": "Nov 23, 2015 5:52:44 PM",
      "itemId": 0,
      "favourites": 0,
      "id": 1,
      "user": {
        "id": 1,
        "name": "Sebastian",
        "surname": "Duque",
        "isAdmin": false
      },
      "content": "The post was removed by a moderator.",
      "parentId": -1
    }
  ],
  ...
}
```

## API

### Users

##### Get all users

``` bash
curl -H "Content-Type: application/json" http://localhost:9998/users -s 
```

##### Get a single user

``` bash
curl -H "Content-Type: application/json" http://localhost:9998/users/0
```

##### Get a user favoured comments

Note that you can see other's as well by specifying an id.

``` bash
curl -H "Content-Type: application/json" http://localhost:9998/users/0/favourites -s
```

##### Change current active user

``` bash
curl -v -H "Content-Type: application/json" -X PUT http://localhost:9998/users/{user_id}/act  
```

##### Get followings items

``` bash
curl -v -H "Content-Type: application/json" http://localhost:9998/users/followings
```

##### Get notifications

``` bash
curl -v -H "Content-Type: application/json" http://localhost:9998/users/notifications
```

##### Mark notifications as read

``` bash
curl -v -H "Content-Type: application/json" -X PUT http://localhost:9998/users/notifications
```

### Items

##### Get all items

``` bash
curl -H "Content-Type: application/json" http://localhost:9998/items -s
```

##### Get a single item

``` bash
curl -H "Content-Type: application/json" http://localhost:9998/items/0 -s
```

##### Create a new item

Note that once you create an item, you will be automatically
follow that item.

``` bash
curl -H "Content-Type: application/json" -X POST -d '{"content":"Turkish goes to the Moon"}' http://localhost:9998/items
```

##### Post a comment to an item

``` bash
curl -H "Content-Type: application/json" -X POST -d '{"content":"New comment"}' http://localhost:9998/items/0/reply
```

##### Follow an item

``` bash
curl -H "Content-Type: application/json" -X POST http://localhost:9998/items/0/follow
```

##### Unfollow an item

``` bash
curl -H "Content-Type: application/json" -X DELETE http://localhost:9998/items/0/unfollow
```

### Comments

##### Get nested comment

Note that this will only return nested comments, not the comment itself.

``` bash
curl -H "Content-Type: application/json" http://localhost:9998/comments/0 -s
```

##### Post a comment to another comment

Aka create a nested comment.

``` bash
curl -H "Content-Type: application/json" -X POST -d '{"content":"New comment"}' http://localhost:9998/comments/0/reply
```

##### Remove a comment

``` bash
curl -H "Content-Type: application/json" -X DELETE http://localhost:9998/comments/2
```

##### Restore a comment

``` bash
curl -H "Content-Type: application/json" -X PUT http://localhost:9998/comments/2/restore
```

##### Favourite a comment

``` bash
curl -H "Content-Type: application/json" -X POST http://localhost:9998/comments/2/favourite
```

##### Unfavourite a comment

``` bash
curl -H "Content-Type: application/json" -X DELETE http://localhost:9998/comments/2/unfavourite
```

## CORS

`CORSResponseFilter` which deal with [HTTP access control (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS)
is credited to student whose id is 14250136!
