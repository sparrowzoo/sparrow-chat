config_path=$(pwd)/config.properties
echo $config_path
sh ./sparrow-java-coder.sh -ct com.sparrow.chat.im.po.Message -config=$config_path -c

