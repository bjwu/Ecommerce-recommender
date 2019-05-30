# 2019.5.27

## **1. 远程ssh连接笔记本**

由于ssh需要网络拥有公网ip，但是公网ip需要向运营商申请，故使用`ngrox`作反向代理进行这项工作

> ngrok 是一个反向代理工具，可以实现将内网的端口暴露到公网，通过 ngrok，也能将 ssh 使用的端口暴露出去，以此实现 ssh 的内网穿透。

官网：https://ngrok.com/download
用户名：bjwu@connect.hku.hk


服务器默认在us，为了更快，选择新加坡节点“ap”

![这里有图片](../images/%20ngrox_tcp.png)


forwarding中，`12569`为给予的端口号，得保持该process一直运行，不然下一次端口号就会变，这就是开源的代价吧。

远程登录操作：
```bash
ssh -p 12569 username@0.tcp.ap.ngrok.io
```

## **2. Ubuntu Installation**

### **2.1. host安装Ubuntu 18.0.4**

我们在host上安装了Ubuntu 18.0.4，[这里](https://morvanzhou.github.io/tutorials/others/linux-basic/1-2-install/)介绍了如何直接把U盘变成ubuntu安装盘，并把windows系统直接换成ubuntu系统。

### 2.2. 安装virtualbox并创建两台linux虚拟机

- 坑1：

  当创建好虚拟机，要载入映像os（提前下载）时发生错误：*kernel driver not installed(rc=-1908)* 

  ![](https://i.stack.imgur.com/WKnp1.png)

  这是因为没有安装或者需要更新 *DKMS( Dynamic Kernel Module Support,动态内核模块)* 。检查是否已经下载以下模块：

  - virtualbox-dkms
  - virtualbox-guest-dkms

  一般来说，可以通过运行以下两句话来解决Ubuntu中的 **Linux kernel update** 问题：

  ```python
  sudo apt-get remove virtualbox-dkms
  sudo apt-get install virtualbox-dkms
  ```

  或者也可以用另一种方式重新安装 *virtualbox-dkms*：

  ```python
  sudo apt-get autoremove virtualbox-dkms
  sudo apt-get install build-essential linux-headers-`uname -r` dkms virtualbox-dkms
  ```

- 坑2

  执行完上述操作之后，仍旧报错：

  ![](https://raw.githubusercontent.com/zifehng/MarkDownPhotos/VirtualBox/vboxdrv_error.png)

  这个错误描述得很清楚，利用 modprobe 命令重新加载模块就好了：

  ```python
  sudo modprobe vboxdrv
  sudo modprobe vboxnetflt
  ```

## **3. Ubuntu Network (Adapter&Host-only)**

VirtualBox中的Ubuntu配置双网卡，在VirtualBox中安装Ubuntu后，默认情况下有一块处于NAT模式的虚拟网卡，若宿主机可以上网，虚拟机也可以通过NAT访问宿主机所在网络，但是却无法实现宿主机和虚拟机之间的互联。
使用双网卡，一块网卡工作在NAT模式，另外一块网卡工作在Host-only模式，这样即实现了虚拟机上外网，又实现了虚拟机与宿主机之间的互联，即使在断网情况下也可以互联。世间也有两全美。

VirualBox有四种虚拟网络连接模式，包括NAT(网络地址转换) 模式、Bridge (桥接)模式、Host-Only(主机)模式、Internal(内网)模式

具体操作：
1. https://www.jianshu.com/p/cc6ed627b5d4

2. https://blog.csdn.net/dream_an/article/details/68484911

ubuntu18与之前的版本在配置网卡的时候不甚相同，ubuntu18的配置文件在`/etc/netplan/`，其格式有很大差别，参考：https://ywnz.com/linuxjc/1502.html

# 2019.5.28

## 1.Hadoop的hdfs关闭后重新开启的问题

由于hdfs关闭后，如果改动了配置，之前残留的data都无法恢复，且之前残留的data会占据掉新的datanode储存的空间，导致datanode无法启动，所以在重新开启hdfs之前，需要删除掉slaves中的data文件

具体路径: `cd /var/hadoop/hadoop-hduser/dfs/data`

将current文件夹删除即可。

## 2. Redis 安装与配置

* 获取redis并编译

  ```bash
  $ wget http://download.redis.io/releases/redis-4.0.14.tar.gz
  $ tar xzf redis-4.0.14.tar.gz
  $ cd redis-4.0.8
  $ make
  $ cd src
  $ make install PREFIX=/usr/local/redis
  ```

* 移动配置文件到安装目录下

  ```bash
  $ mkdir /usr/local/redis/etc
  $ mv redis.conf /usr/local/redis/etc
  ```

* 配置redis为后台启动

  ```bash
  $ vim /usr/local/redis/etc/redis.conf #将daemonize no 改成daemonize yes
  ```

* Reference: https://segmentfault.com/a/1190000017370688

## 3. Redis, Hbase & Mongodb比较

> 当SQL满足不了你的需求或者SQL 已经不是必须的或者最佳的选择时，就是你考虑这类NoSQL 的时候了。
当你的内存大于你的数据时，schema也不是太确定时，mongodb在这里静静地等待MySQL转业户为了尝鲜过来看热闹的，不改变设计模式，爽在前面痛在后面;
当你唯一追求的就是速度，又对memcached的过于简单心存芥蒂，刚好内存也比数据多时，redis俏生生站在那里;大，好大，太大了，我说的是数据，我们128GB内存双路CPU25TB存储只够一星期的时候，估计就没有选择综合症了，HBase成了唯一或者唯二选择了。
所以呢，不严谨地讲，Redis定位在"快"，HBase定位于"大",mongodb定位在"灵活"。
NoSQL的优点正好就是SQL的软肋，而其弱点正好也就是SQL的杀手锏
最大区别在于，在一般使用情况下，mongodb可以当作简单场景下的但是性能高数倍的MySQL, Redis基本只会用来做缓存，HBase用来做离线计算


# 2019.5.29

1. 杀不掉redis进程 

> 不知道为什么kill -9 命令无法杀掉 redis 进程 ，杀掉就自动重启 
redis-cli shutdown 也不行 
redis-cli -h 127.0.0.1 -p 6379 shutdown 都不行 
最后这个命令能杀掉 
/etc/init.d/redis-server stop

2. Jedis 错误

> MISCONF Redis is configured to save RDB snapshots, but is currently not able to persist on disk. Commands that may modify the data set are disabled. Please check Redis logs for details about the error

将stop-writes-on-bgsave-error设置为no

```bash
127.0.0.1:6379> config set stop-writes-on-bgsave-error no
```


# 2019.5.30

## 1. Samba

- Samba 是一个基于 GPL协议 的自由软件，可以实现各个平台(mac，linux，windows）共享文件。

## 1.1. mac和linux服务器共享文件

- 在linux服务器上设置一个共享文件夹，mac可以直接上传或下载文件。

## 1.2. linux部署

- 安装 samba

  我的linux服务器是ubuntu，包管理器是 apt-get

  ```python
  $ sudo apt-get samba
  ```

- 设置共享文件夹

  创建一个文件夹 shared_directory 作为共享文件夹（也可以选择已有的，无需创建），并设置该文件夹权限为所有用户可读写

  ```python
  $ mkdir /home/USER_NAME/shared_directory
  $ sudo chmod 777 /home/USERNAME/shared_directory
  ```

  这里的USERNAME是你的linux服务器名

- 配置 samba.conf

  直接修改 `/etc/samba/smb.conf` 文件，在文件末尾添加：

  ```python
  [share]
        path = /home/USERNAME/shared_directory
        available = yes
        browseable = yes
        public = yes
        writable = yes
  ```

- 添加 samba 账户（即允许访问该共享文件夹的用户）

  ```python
  $ sudo touch /etc/samba/smbpasswd
  $ sudo smbpasswd -a CLIENTNAME
  ```

  这里的CLIENTNAME就是可访问的用户。这里会要求输入密码，记住对象的用户名和密码，之后需要远程登录。
  这里需要注意，有可能会报错：*Failed to add entry for user* 。很有可能是用户名不合法或者已经有该用户等错误信息，只要尝试改几次用户名就可以了。

## 1.3. mac连接

- 打开finder或者桌面，快捷键 `command + k `，得到以下界面：

  ![](https://raw.githubusercontent.com/zifehng/MarkDownPhotos/VirtualBox/samba1.png)

  

  在 `smb://` 后面输入你想要连接的服务器地址。我这里是局域网内的，你也可以是任意可以ping通的服务器地址

  选择 **注册用户** 并输入前面的CLIENTNAME和密码，完成登录。可以看到该共享文件了。 

  
## 2. linux下运行jar
- 在Linux系统下直接执行java -jar XXX.jar， 往往会提示：No main manifest attribute, in XXX.jar 错误
- 正常情况下，java打包成jar包需要在MANIFEST.MF中指定MainClass项以便运行java -jar XXX.jar时找到对应的主类。虽然我们在IntelliJ打包的时候选择了主类，但似乎linux系统并不买账
- 利用 `java -cp XXX.jar MAINCLASSNAME` 指定主类名就可以正常
  











