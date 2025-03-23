systemctl daemon-reload  #(重新reload开机启动所有配置)
systemctl enable supervisord #（允许supervisord服务开机自启动）
systemctl restart supervisord #（启动supervisord）
systemctl status supervisord #（查看状态）
