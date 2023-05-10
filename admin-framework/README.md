# admin-framework鉴权模块逻辑

## 登录鉴权模块业务逻辑
1. 注册功能逻辑：
   a. 校验验证码是否正确，校验用户名秘密是否符合要求，用户信息保存（密码需加密保存），最后记录注册成功的日志
   b. 前端注册或者登录操作时会带上唯一标识uuid，该标识在后端生成验证码时传给前端，用户登录时/注册时会带上用于做验证码的校验：
   c.【校验过程为生成验证码会以 uuid -> 验证码正确结果 形式保存到redis，而后uuid会发送到前端用户填写验证码结果提交时，会拿uuid去redis取正确验证码结果与用户填写的验证码结果对比。】
2. 登录功能逻辑：
   a. 验证码校验，登录用户名密码以及IP黑名单校验，调用springSecurity的AuthenticationManager进行用户校验
   b.（AuthenticationManager会调用UserDetailService进行校验，开发者可以实现UserDetailService接口自定义校验逻辑）
   c. UserDetailService.loadUserByUsername中查询已注册的用户进行用户名密码比对，密码校验（设定校验次数，连续校验多次不通过让用户等待一段时间再尝试登录）
   d. UserDetailService.loadUserByUsername方法校验无误会查询用户的菜单权限信息以及用户注册时保存的所有信息返回，
   e. 上述函数返回说明校验成功了，记录用户登录信息
   f. 生成uuid（随机生成一个uuid作为redis的key,redis该key的value为前面校验成功并返回的用户所有信息和用户拥有的菜单权限信息，并设置半小时的超时时间）
   g. 采用JWT工具对上述生成的uuid进行编码生成token，然后登录方法带上这个token返回给前端。【这里生成token的内容可以不用uuid，而是用部分用户信息转成json数据，再采用JWT工具生成token】
   h. 登录之后，后续用户每次向后端发送请求都会先经过filter拦截器的拦截【JwtAuthenticationTokenFilter】，拦截器中先从用户http请求头中特定字段拿到token
   i. 拿到的token先解码解密得到uuid，然后查redis该uuid拿到用户的详细信息和权限信息，同时要刷新redis中该uuid的过期时间。
3. 权限校验功能逻辑：



org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder采用加盐和随机字符拼接后再hash的方法进行加密存储用户密码，因此无法通过查看用户数据库获取用户密码。