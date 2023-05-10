# 业务逻辑

## 关联关系设定

            角色
          /  |  \
         /   |   \
     部门1   用户1  菜单1 
     部门2   用户2  菜单2
     ...     ...    ...
             |  \
             |   \
            岗位1  角色1
            岗位2  角色2
             ...    ...

            部门
          /  |  
         /   |  
     部门1   用户1
     部门2   用户2

## 系统管理
### 登录
- /captchaImage   生成验证码和uuid返回给前端同时保存【uuid- 验证码正确结果】到redis
- /login          登录接口，将用户传入的用户名密码与数据库数据进行对比校验，同时redis中保存【该用户名-该用户密码重试次数】，重试超过配置的特定次数之后用户登录会被锁定一定的时间（可配置）
- /getInfo        登录成功后获取用户信息，包括用户（角色/部门）信息，角色信息，菜单权限信息
- /getRouters     登录成功后查询该用户所拥有的所有角色所拥有的所有菜单权限进行展示，菜单路径保存在表sys_menu

### 用户管理
- /system/user/list?pageNum=1&pageSize=10         分页查询用户（用户信息以及用户所在部门的详细信息）
- /system/user/deptTree                           查询部门树
- /system/dict/data/type/sys_normal_disable       字典数据查询
- /system/dict/data/type/sys_user_sex             字典数据查询
- /system/config/configKey/sys.user.initPassword  配置数据查询
- GET -- /system/user/                            新增用户前，填写用户信息前先提供系统中已有的所有岗位和角色用于选择
- POST -- /system/user/                           新增用户
- /system/user/changeStatus                       用户状态改变 status = 0-正常 1-停用
- /system/user/resetPwd                           用户密码更新
- GET -- /system/user/{userId}                    更新用户前先查询用户详细信息，和上面是同一个controller
- PUT -- /system/user                             更新用户信息
- DELETE -- /system/user/{userId}                 删除用户
- GET -- /system/user/authRole/{userId}           给用户分配角色前，先查询角色
- PUT -- /system/user/authRole                    给用户分配角色（插入表sys_user_role，一个用户可以有多个角色）
- /system/user/export                             导出用户列表到Excel
- /system/user/importTemplate                     下载模版，提供给操作者填写数据便于导入
- /system/user/importData?updateSupport=true      从Excel列表导入用户数据到系统，updateSupport为true支持更新

### 角色管理
- /system/role/list?pageNum=1&pageSize=10         分页查询角色数据
- /system/menu/treeselect                         查询菜单树，用于在新增角色时设定该角色具有的菜单权限
- POST -- /system/role                            新增角色
- /system/menu/roleMenuTreeselect/{roleId}        修改角色前，查询角色菜单树的列表，一个角色拥有多个菜单
- /system/role/{roleId}                           修改角色前，查询角色信息
- PUT -- /system/role                             修改角色
- /system/role/changeStatus                       改变角色的状态 status = 0-正常 1-停用
- /system/role/deptTree/{roleId}                  角色数据权限修改前，查询该角色拥有的部门
- /system/role/{roleId}                           角色数据权限修改前，查询角色信息
- PUT -- /system/role/dataScope                   角色数据权限修改（插入表sys_role_dept）
- /system/role/authUser/allocatedList?pageNum=1&pageSize=10&roleId={roleId}      分配用户，先分页查询已经分配了该角色的用户
- /system/role/authUser/unallocatedList?pageNum=1&pageSize=10&roleId={roleId}    分配用户，分配用户前查询还未分配该角色的用户
- /system/role/authUser/selectAll?roleId={roleId}&userIds={userId},{userId}      批量选择用户授权
- /system/role/authUser/cancel                                                   取消某个用户的角色授权（表sys_user_role）
- /system/role/authUser/cancelAll?roleId=101&userIds=102                         批量取消选定部门用户的角色授权（表sys_user_role）

### 菜单管理
- /system/menu/list                               查询所有菜单列表（菜单树）
- /system/menu/list                               新增菜单前，查询所有菜单层级 为新建的菜单提供依赖层级
- POST -- /system/menu                            新增菜单
- /system/menu/{menuId}                           修改菜单前查询该菜单详细信息
- PUT -- /system/menu                             修改菜单信息
- DELETE -- /system/menu{menuId}                  删除菜单

### 部门管理
- /system/dept/list                               查询所部门列表（部门树）
- /system/dept/list/exclude/{deptId}              修改部门前查询排除该部门的其他部门节点
- /system/dept/{deptId}                           修改部门前查询该部门的详细信息
- PUT -- /system/dept                             修改部门
- /system/dept/list                               新增部门前查询所部门列表（部门树）
- POST -- /system/dept                            新增部门
- DELETE -- /system/dept/{deptId}                 删除部门

### 岗位管理
- /system/post/list?pageNum=1&pageSize=10         分页查询岗位
- POST -- /system/post                            新增岗位
- /system/post/{postId}                           修改岗位前查询该岗位详细信息
- PUT -- /system/post                             修改岗位
- DELETE -- /system/post/{postIds}                删除岗位，支持单个和批量删除

### 字典管理
- /system/dict/type/list?pageNum=1&pageSize=10    分页查询字典类型数据（sys_dict_type）
- /system/dict/type/{dictId}                      查询字典类型详细
- /system/dict/type/optionselect                  获取字典类型选择框列表（查询表sys_dict_type所有数据）
- /system/dict/data/list?pageNum=1&pageSize=10&dictType=sys_user_sex    根据字典名称（其编码为字典类型）分页查询字典数据
- /system/dict/type/{dictId}                      修改字典类型前查询该字典类型的详细信息
- PUT -- /system/dict/type                        修改字典类型
- DELETE -- /system/dict/type /{dictIds}          删除字典类型信息
- /system/dict/data/...                           操作字典类型下的字典数据（sys_dict_data）

### 参数设置
- /system/config/list?pageNum=1&pageSize=10       分页查询系统配置参数
- /system/config/refreshCache                     从redis刷新系统配置参数
- /system/config/{configId}                       根据参数编号获取详细信息
- PUT -- /system/config                           修改参数配置
- POST -- /system/config                          新增参数配置
- DELETE -- /system/config/{configIds}            删除参数配置

### 通知公告
- /system/notice/list                             获取通知公告列表
- /system/notice/{noticeId}                       根据通知公告编号获取详细信息
- POST -- /system/notice                          新增通知公告
- PUT -- /system/notice                           修改通知公告
- DELETE -- /system/notice/{noticeIds}            删除通知公告

### 日志管理
- /monitor/operlog/list?pageNum=1&pageSize=10     分页查询系统操作日志
- /monitor/logininfor/list?pageNum=1&pageSize=10  分页查询登录日志
- /monitor/logininfor/unlock/{userName}           账户解锁（账户登录多次密码错误锁住之后，手动解锁）

### 在线用户
- /monitor/online/list                            查看在线用户列表
- /monitor/online/{tokenId}                       强退在线用户

### 服务监控
- /monitor/server                                 服务机器信息监控

### 缓存监控/缓存列表
- /monitor/cache                                  缓存信息监控
- /monitor/cache/getNames                         缓存列表查询
- ...                                             缓存键的删除等操作




