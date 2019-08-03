# springboot-util

SpringBootで新しいプロジェクトを作るときのサポート用

## 新規作成手順

新規プロジェクトはSpring Initializrで作成する。

公式サイト

https://start.spring.io/

vscode拡張

https://github.com/microsoft/vscode-spring-initializr

### 登録する依存関係

* Spring Boot DevTools
* Lombok
* Spring Web Starter
* H2 Database
* JDBC API
* Flyway Migration

## 設定ファイル更新

1. スクリプトダウンロード
```
curl -L -O https://raw.githubusercontent.com/yfujii01/springboot-util/master/script.bash
```

2. 設定ファイル更新
```
bash script.bash setting
```
