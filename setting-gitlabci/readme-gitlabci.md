# gitlab ciについて

## 概要

GitLabにアップロードすることでCIを実行してくれる

privateリポジトリについても1ヶ月2000分無料で使用できる

具体的には以下のような運用が可能

1. GitLabに変更をpush

2. GitLab CIが起動してクラウド上でテストを実施

3. 成功 or 失敗を通知したり、プルリクエストの画面でテストが成功しているか分かるようになる

## 設定方法

1. ./.gitlab-ci.ymlをプロジェクトに配置する

2. gitlabにuploadする

## バッジの設定方法

### GitLabのプロジェクトにせっていする

1. プロジェクト > 設定 > 一般 > バッジ

2. pipelineバッジの設定を行う
    * リンク
    <https://gitlab.com/%{project_path}>
    * バッジ画像のURL
    <https://gitlab.com/%{project_path}/badges/%{default_branch}/pipeline.svg>

3. coverageバッジの設定を行う
    * リンク
    <https://gitlab.com/%{project_path}>
    * バッジ画像のURL
    <https://gitlab.com/%{project_path}/badges/%{default_branch}/coverage.svg>

4. プロジェクト > 設定 > CI\CD > 一般のパイプライン > テストカバレッジ解析

    ```reg
    ^ - branch\s*:\s+(\d+\.\d+)%$
    ```

    ※テストカバレッジをCI中にどういう形式で出力しているかを記載する
