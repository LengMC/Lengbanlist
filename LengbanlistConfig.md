prefix: "§f§l[§bLengbanlist§f§l]"
sendtime: 5 #已分钟为单位
opensendtime: true #开启循环播报封禁人数
muted:
Model: "Default"
valid-models: "Default HuTao Furina Zhongli Keqing Xiao Ayaka Zero Herta"
language: "default" # 当前默认语言，可选值：default, en_US
# 当前可用的语言列表：
# - default: 默认语言（中文）
# - en_US: 英文
database:
  type: "sqlite"  # 或 "mysql"
  mysql:
    host: "localhost"
    port: 3306
    database: "lengbanlist"
    username: "root"
    password: "password"
