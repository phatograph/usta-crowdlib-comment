## Introduction

To run the unit test, cobertura code coverage and checkstyle tasks use the 
following commands on the command line ($ denotes the command prompt) or their
equivalents in Eclipse.

Tests:

``` bash
$ gradle test
$ open build/reports/tests/index.html
```

Cobertura:

``` bash
$ gradle cobertura
$ open build/reports/cobertura/index.html
```

Checkstyle:

``` bash
$ gradle check
$ open build/reports/checkstyle/main.html
```

## API

### Change current user

``` bash
$ curl -v -H "Content-Type: application/json" -X PUT http://localhost:9998/users/1/act  
```