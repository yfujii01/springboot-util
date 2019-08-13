[HOMEへ](../readme.md)

# curlを使ってSpringInitialyzrを使う方法

## サンプル
```
$ curl https://start.spring.io/starter.zip -d dependencies=web,devtools -d bootVersion=2.1.2.RELEASE -o my-project.zip
```

## よく使う構成
```
$ curl https://start.spring.io/starter.zip -d type=gradle-project -d dependencies=web,devtools,lombok,h2,jdbc,flyway -o demo.zip

$ unzip demo.zip -d demo

$ rm demo.zip
```

## ヘルプ

```
$ curl https://start.spring.io
```

2019/8/12時点の内容をhelp.txtに保存済み

## 設定可能な項目一覧

```
$ curl -H 'Accept: application/json' https://start.spring.io
```

2019/8/12時点の内容をhelp.jsonに保存済み
