config_path=$(pwd)/config.properties
echo $config_path
sh ./sparrow-java-coder.sh -ct com.sparrow.chat.contact.po.Qun -config=$config_path
sh ./sparrow-java-coder.sh -ct com.sparrow.chat.contact.po.QunMember -config=$config_path
sh ./sparrow-java-coder.sh -ct com.sparrow.chat.contact.po.Contact -config=$config_path
sh ./sparrow-java-coder.sh -ct com.sparrow.chat.contact.po.Audit -config=$config_path


