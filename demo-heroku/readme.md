# herokuデプロイ手順

SpringBootアプリケーションをherokuにデプロイするのはとても簡単

2019/8/12時点ではprocfileすら不要になっている

SpringInitializrで作成したプロジェクトをherokuにアップするだけでデプロイが完了する。


### 事前準備1

* herokuのアカウントを作成しておく

https://dashboard.heroku.com


### 事前準備2

* 端末にheroku-cliをインストールし、ログインしておくこと

https://devcenter.heroku.com/articles/heroku-cli


* Raspberry PiなどARM機器にインストールするには以下の手順
※ windowsなどではnpmによるインストールは推奨されていない(動くけど)

```sh
# npmでインストール
$ npm i -g heroku

# ブラウザーを使わずにログイン
$ heroku login -i
heroku: Enter your login credentials
Email: me@example.com
Password: ***************
Two-factor code: ********
Logged in as me@heroku.com
```

### プロジェクト作成

```sh
# ディレクトリ作成
$ mkdir spring-heroku-sample

$ cd spring-heroku-sample

# プロジェクト作成
$ curl https://start.spring.io/starter.zip -d type=gradle-project -d dependencies=web,devtools,lombok -o demo.zip

$ unzip demo.zip -d demo

$ rm demo.zip

$ cd demo
```

### herokuへデプロイ

```sh
# git管理
$ git init ; git add -A ; git commit -m init

# herokuプロジェクト作成
$ heroku create
# プロジェクト名を指定する場合は
# $ heroku create hoge_project

# デプロイ
$ git push heroku master
```
