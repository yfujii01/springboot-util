function help(){ # コマンド一覧を表示する
    cat ./script.bash | grep "^function [A-z]"
}
function setting(){ # settingファイルを独自のものに置き換える
    curl -L -O https://github.com/yfujii01/springboot-util/archive/master.zip
    unzip -o master.zip

    # macのcpには-Tが無いため使用不可 
    # cp -rT ./springboot-util-master/setting .

    # *指定でドットファイルのコピーも行うように設定
    shopt -s dotglob
    cp -a ./springboot-util-master/setting/* .
    # *指定でドットファイルのコピーを行わないように再設定
    shopt -u dotglob

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
