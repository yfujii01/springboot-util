# demo-security

## 起動方法

jwtのパスワードを環境変数から読み取っているため設定が必要。

src/main/java/com/example/demo/security/SecurityConstants.java

### vscodeで起動する場合

1. launch.jsonを設定する

.vscode/launch.json
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Debug (Launch)-DemoApplication<demo>",
      "request": "launch",
      "mainClass": "com.example.demo.DemoApplication",
      "projectName": "demo",
      "env": { "security_password": "hogehoge" }
    }
  ]
}
```

2. F5で起動

### ターミナルで起動する場合

1. 環境変数を設定する

```sh
$ export security_password=hogehoge
```

2. 起動する

```sh
$ gradle bootrun
```

## 機能説明

1. /publicは自由にアクセス可能

GET http://localhost:8080/public

```
HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 20
Date: Thu, 08 Aug 2019 22:03:44 GMT
Connection: close

this is public page!
```

2. /privateはtokenを設定しないとアクセスできない

GET http://localhost:8080/private

```
HTTP/1.1 403 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 08 Aug 2019 22:04:26 GMT
Connection: close

{
  "timestamp": "2019-08-08T22:04:26.606+0000",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/private"
}
```

3. /loginにusernameとpasswordを渡すことでAuthorizationにtokenが設定して返される

POST http://localhost:8080/login
Content-Type: application/json

{
  "username":"hoge",
  "password":"fuga"
}

```
HTTP/1.1 200 
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2dlIiwiZXhwIjoxNTY1MzMwNjg2fQ.l9VB8SQUDniQUuspeTTZHvFghrBx5c51KqSbDWDBkqbYJivUW97OG5J-irQrDz8iACGEah0prIN0j9wnJou1VQ
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Thu, 08 Aug 2019 22:04:47 GMT
Connection: close
```

4. tokenを設定して/privateにアクセス

GET http://localhost:8080/private
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2dlIiwiZXhwIjoxNTY1MzMwNjg2fQ.l9VB8SQUDniQUuspeTTZHvFghrBx5c51KqSbDWDBkqbYJivUW97OG5J-irQrDz8iACGEah0prIN0j9wnJou1VQ

```
HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 21
Date: Thu, 08 Aug 2019 22:07:32 GMT
Connection: close

this is private page!
```

