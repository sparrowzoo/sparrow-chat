#!/bin/sh

source /etc/profile

[ -z "$SPARROW_CODER_HOME" ] && echo "please config environment variable SPARROW_CODER_HOME" && exit 0

# 生成ddl
# sh coder.sh -ct com.sparrow.chat.po.Qun
#


sparrow_coder_name=sparrow-coder-all.jar
class_path=$(cd ../target/classes;pwd)
#protocol_path=~/workspace/sparrow/sparrow-shell/sparrow-protocol-dao/target/sparrow-protocol-dao-1.0.0-SNAPSHOT.jar
java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.Main $@



