echo "reload systemctl config"
systemctl daemon-reload
echo "enable supervisord boot when system start"
systemctl enable supervisord
echo "restart supervisord"
systemctl restart supervisord
echo "check supervisord status"
systemctl status supervisord
echo "tail supervisord log"
cd /var/log/supervisor
tail -f supervisord.log
echo "tail sparrow chat console log"
tail -f sparrow_chat_console_out.log
