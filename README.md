# TeleportTweaker
**1.12.2版Sponge(Sponge API 7.2.0)** 用のプラグインです(**他のバージョンでの動作はサポート外になります**)  
バニラの/tpコマンドにいくつかの機能を追加しています

## 使い方
他のプラグインと同様に、modsフォルダーにjarファイルを追加してください。  
デフォルトのtpとコマンドが衝突してしまうので、 ```config/sponge/global.conf``` で明示的にこちらのプラグインを使うように設定します

```
    # Configuration options related to commands, including command aliases and hidden commands.
    commands {
        # Command aliases will resolve conflicts when multiple plugins request a specific command.
        # Correct syntax is '<unqualified command>=<plugin name>' e.g. 'sethome=homeplugin'
        aliases {
            tp=tptweaker
        }
        ...
    }
```
ファイルの中から上記の部分を探して、変更してください  

## コマンド
/allowtp info [Player]  
プレイヤーのTP許可に関する情報を取得、何も指定しなかったときは自身の情報  
  
/allowtp set <true|false> [-p]  
自身のTP許可を設定 -pはリログ後も設定を継続するかどうか

また、このプラグインを入れると異なるディメンション間でもプレイヤーに飛べるようになります

## ビルド方法
release版以外は動作する保証はできませんが、自分でビルドすることで最新版が手に入ります。  
ダウンロードして解凍し、ディレクトリ上でコマンドラインに移動し以下のコマンドを入力してください  
```gradlew clean build```  
ビルドされたファイルは ```/build/libs``` 内に入っています

## 連絡先
バグなどの報告はIssuesにてお願いします。それ以外で私に連絡する必要がある場合は、Twitter([@chilage125](https://twitter.com/chikage125))で連絡してもらえるとある程度は対応できるかと思います。  
