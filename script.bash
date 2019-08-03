function help(){ # コマンド一覧を表示する
    cat ./script.bash | grep "^function [A-z]"
}
function setting(){ # settingファイルを独自のものに置き換える
    # スクリプトダウンロード
    curl -L -O https://raw.githubusercontent.com/yfujii01/springboot-util/master/script.bash
    
    # application.properties
    curl -L -O https://raw.githubusercontent.com/yfujii01/springboot-util/master/application.properties
    mv application.properties ./src/main/resources/application.properties

    # flyway用マイグレーションSQL
    curl -L -O https://raw.githubusercontent.com/yfujii01/springboot-util/master/V1__Initail_DB.sql
    mkdir ./src/main/resources/db/migration
    mv V1__Initail_DB.sql ./src/main/resources/db/migration/V1__Initail_DB.sql

}

# 引数に指定した関数を実行する
for x in "$@"
do
    set -x
    $x
    set +x
done

# 引数が指定されない場合はhelpを実行する
if [ -z "$@" ]; then
    set -x
    help
    set +x
fi
