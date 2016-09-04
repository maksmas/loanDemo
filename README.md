# loanDemo
mmm loan demo

## build & run
**Linux:** ./gradlew build bootRun

**Win:** gradlew.bat build bootRun (maybe, not sure)

## links
**apply for loan:** POST localhost:8080/loans/
>{
>    "amount": 100,
>    "term": 7,
>    "user": {
>        "firstName": "Robin",
>        "lastName": "Hood",
>        "personalId": "111111-1111"
>    }
>}

**list all approved loans:** GET localhost:8080/loans/

**list all approved loans by user:** GET localhost:8080/loans/byUser?firstName=Robin&lastName=Hood

## PS
1. default auth -> user/user
2. using country resolving service with 200 cals/day limit. (added cache as workaround)
3. using embedded derby, so on restart all data will be lost 
