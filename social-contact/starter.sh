echo "reload systemctl config"
systemctl daemon-reload
echo "enable supervisord boot when system start"
systemctl enable supervisord
echo "restart supervisord"
systemctl restart supervisord
echo "check supervisord status"
systemctl status supervisord
