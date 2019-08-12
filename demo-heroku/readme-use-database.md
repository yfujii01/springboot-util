# herokuデプロイ手順(DBを使用する場合)

### 事前準備

[事前準備1 & 2を完了していること](./readme.md)

### プロジェクト作成

```sh
# プロジェクト作成
$ curl https://start.spring.io/starter.zip -d type=gradle-project -d dependencies=web,devtools,lombok,h2,jdbc,flyway,postgresql -o demo.zip

# プロジェクト展開
$ unzip demo.zip -d spring-heroku-db-sample ; rm demo.zip ; cd spring-heroku-db-sample

# git管理
$ git init ; git add -A ; git commit -m init
```

### 設定

```sh
# DB設定追加

application.properties

V1__Initail_DB.sql

HogeController.java
```

### herokuへデプロイ

```sh
# herokuプロジェクト作成
$ heroku create

# デプロイ
$ git push heroku master
```

### プロジェクト削除

```sh
$ heroku destroy
```