# circle ciについて

## 概要

CIを実行してくれるSaaS

GitHubとの連携が簡単で無料で使用できる

具体的には以下のような運用が可能

1. GitHubに変更をpush

2. circle ciが起動してクラウド上でテストを実施

3. 成功 or 失敗を通知したり、プルリクエストの画面でテストが成功しているか分かるようになる

## 設定方法

1. ./.circlci/config.ymlをプロジェクトに配置する

2. githubにuploadする

3. circle ciにログインする

https://circleci.com/

Log In with GitHub でOK

4. Add PROJECTS

5. Set Up Project

初期設定でOK

* Operating System
	Linux
* Language
	Gradle(Java)

6. Start building

## バッジの設定方法

### public プロジェクトの場合

1. circle ciのプロジェクトの設定画面

2. NOTIFICATIONS > Status Badges

3. Embed Code に書かれているコードをプロジェクトのreadme.mdの先頭に記載する

### private プロジェクトの場合

1. circle ciのプロジェクトの設定画面

1. PERMISSIONS > API Permissions

1. Create Token

* First choose a scope and then create a label.
	Status

* Token Label
	Demo Project Circle Ci Status Badge

1. NOTIFICATIONS > Status Badges

1. API Token
先程作成したTokenを選択する

1. Embed Code に書かれているコードをプロジェクトのreadme.mdの先頭に記載する
