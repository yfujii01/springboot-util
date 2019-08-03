function help(){ # コマンド一覧を表示する
    cat ./script.bash | grep "^function [A-z]"
}
function setting(){ # settingファイルを独自のものに置き換える
    curl -L -O https://github.com/yfujii01/springboot-util/archive/master.zip
    unzip master.zip
    cp -r springboot-util-master/setting/* .
    rm master.zip
    rm -rf springboot-util-master
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
