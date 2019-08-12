# vscodeで開発するために

## デバッグ設定

.vscode/launch.json にデバッグ設定を行う

F5を押すことで初期作成はしてくれる

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Debug (Launch)-DemoApplication<demo>",
      "request": "launch",
      "mainClass": "com.example.demo.DemoApplication",
      "projectName": "demo"
    }
  ]
}
```