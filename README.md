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
