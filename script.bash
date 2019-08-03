function help(){ # コマンド一覧を表示する
    cat ./script.bash | grep "^function [A-z]"
}
function setting(){ # settingファイルを独自のものに置き換える
    # スクリプトダウンロード
    curl -L -O https://raw.githubusercontent.com/yfujii01/springboot-util/master/script.bash
    
    # application.properties
    curl -L -O https://raw.githubusercontent.com/yfujii01/springboot-util/master/application.properties
    mv ./src/main/resources/application.properties

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
