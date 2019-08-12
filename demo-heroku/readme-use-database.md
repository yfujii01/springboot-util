# herokuデプロイ手順(DBを使用する場合)

### 事前準備

[事前準備1 & 2を完了していること](./readme.md)

### プロジェクト作成

```sh
# プロジェクト作成
$ curl https://start.spring.io/starter.zip -d type=gradle-project -d dependencies=web,devtools,lombok,h2,jdbc,flyway,mysql -o demo.zip

# プロジェクト展開
$ unzip demo.zip -d spring-heroku-db-sample ; rm demo.zip ; cd spring-heroku-db-sample

# git管理
$ git init ; git add -A ; git commit -m init
```

### 設定

* SpringBoot Project設定
```sh
# #######################################################
# 以下のコマンドを実行することでファイルが作成(ダウンロード)される
# ./src/main/resources/application.properties
# ./src/main/java/com/example/demo/controller/HogeController.java
# ./src/main/resources/db/migration/V1__Initail_DB.sql
# #######################################################

# githubからソース取得
$ curl -L -O https://github.com/yfujii01/springboot-util/archive/master.zip

# 解凍
$ unzip master.zip -d tmp

# 必要ファイルをコピー
$ cp -r ./tmp/springboot-util-master/demo-heroku/setting/src/ .

# 作業ファイルを削除
$ rm master.zip ; rm -rf tmp/

# git commit
$ git add -A ; git commit -m 'add heroku db setting'
```

### herokuへデプロイ

```sh
# herokuプロジェクト作成
$ heroku create

# herokuにmysqlアドオンを追加
$ heroku addons:create jawsdb:kitefin

# デプロイ
$ git push heroku master
```

### プロジェクト削除

```sh
$ heroku destroy
```