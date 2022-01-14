# Android期末作品说明文档

### 嘀哩嘀哩 原型

# 1. 设计数据库

- 轮播图信息表
- 用户信息表
- 电影信息表
- 收藏夹表
- 播放历史表

# 2. 设计接口 

- 用户信息接口
  - 登录
  - 注册
  - 修改密码
  - 设置密保
  - 修改用户信息
- 电影信息接口
  - 查询所有电影信息
  - 根据电影名称 类型 演员查询
  - 根据id获取电调电影信息
- 轮播图接口
  - 获取轮播图信息
- 收藏夹接口
  - 收藏
  - 取消收藏
- 历史记录接口
  - 添加历史记录
  - 删除历史记录

### 调用接口地址：http://yanling.gz2vip.idcfengye.com/

##  登录

- user/login

- post

- 请求参数

  {
      "username": "root",
      "password": "root"
  }

- 返回参数

  {

    "code": 200,

    "msg": "请求成功",

    "data": "root"

  }

##  注册

- user/register

- post

- 请求参数

  {
      "username": "root",
      "password": "root"
  }

- 返回参数

  {

    "code": 200,

    "msg": "请求成功",

    "data": **null**

  }

## 根据用户名查询用户信息

- user/getUserInfoByName

- post

- 请求参数

  {

    "username": "养猪散户"

  }

- {

  "code": 200,

  "msg": "请求成功",

  "data": {

  ​    "id": 4,

  ​    "username": "养猪散户",

  ​    "password": "666",

  ​    "email": "未设置",

  ​    "type": "普通会员",

  ​    "gender": "未设置",

  ​    "intro": "未设置"

    }

  }

## 修改邮箱地址

- user/updateEmail

- post

- 请求参数

  {

    "id": 4,

    "email": "111@qq.com"

  }

- 返回参数

  {

    "code": 200,

    "msg": "请求成功",

    "data": 1

  }

## 修改密码 password /性别 gender / 个人简介 intro 同上

## 查询所有电影

- movie/getAllMovies

- get

- 返回参数

  {

    "code": 200,

    "msg": "请求成功",

    "data": [

  ​	{

  ​      "movieId": 1,

  ​      "movieName": "少年的你",

  ​      "movieImage": "https://img1.doubanio.com/view/photo/m/public/p2546068467.webp",

  ​      "cast": "周冬雨 易烊千玺",

  ​      "releaseYear": 2019,

  ​      "country": "中国",

  ​      "type": "剧情",

  ​      "movieUrl": "https://vt1.doubanio.com/202106261349/cc62b23d9c87ed586458317205ab7ebe/view/movie/M/402470194.mp4",

  ​      "movieCover": "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2572166063.webp",

  ​      "movieIntro": "陈念（周冬雨 饰）是一名即将参加高考的高三学生，同校女生胡晓蝶（张艺凡 饰）的跳楼自杀让她的生活陷入了困顿之中。胡晓蝶死后，陈念遭到了以魏莱（周也 饰）为首的三人组的霸凌，魏莱虽然表面上看来是乖巧的优等生，实际上却心思毒辣，胡晓蝶的死和她有着千丝万缕的联系。",

  ​      "director": "曾国祥"

  ​    }

  ]

  }

  ## 根据电影名字 演员名字 电影类型 模糊查询

  - movie/getMovies

  - post

  - 请求数据 (电影名字传movieName, 电影类型传type)

    {

      "cast": "周冬雨"

    }

  - 返回参数

    {

      "code": 200,

      "msg": "请求成功",

      "data": [

    ​    {

    ​      "movieId": 1,

    ​      "movieName": "少年的你",

    ​      "movieImage": "https://img1.doubanio.com/view/photo/m/public/p2546068467.webp",

    ​      "cast": "周冬雨 易烊千玺",

    ​      "releaseYear": 2019,

    ​      "country": "中国",

    ​      "type": "剧情",

    ​      "movieUrl": "https://vt1.doubanio.com/202106261349/cc62b23d9c87ed586458317205ab7ebe/view/movie/M/402470194.mp4",

    ​      "movieCover": "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2572166063.webp",

    ​      "movieIntro": "陈念（周冬雨 饰）是一名即将参加高考的高三学生，同校女生胡晓蝶（张艺凡 饰）的跳楼自杀让她的生活陷入了困顿之中。胡晓蝶死后，陈念遭到了以魏莱（周也 饰）为首的三人组的霸凌，魏莱虽然表面上看来是乖巧的优等生，实际上却心思毒辣，胡晓蝶的死和她有着千丝万缕的联系。",

    ​      "director": "曾国祥"

    ​    }

      ]

    }

    ## 根据电影id 电影类型查询同上

    请求参数分别为

    {

      "movieId": 1

    }

    

    {

      "type": "剧情"

    }

# 3. 编写代码 

### 功能说明

- 欢迎页面
  - 登录
    - 登录
    - 注册
    - 找回密码
  - 注册
    - 注册
    - 登录
  - 找回密码
    - 找回密码
  - 跳过进入首页

- 首页
  - 电影轮播
  - 欢迎标语
    - 未登录状态不显示
    - 登录状态显示用户名及头像
  - 按名称、演员、类型搜索
    - 搜索页面
      - 显示搜索结果数量
      - 搜索结果列表
        - 电影详情
          - 电影信息
          - 收藏/取消收藏(未登录状态跳转登陆界面)
          - 播放视频
  - 按电影类型分类列表
    - 电影列表
      - 电影详情
        - 电影信息
        - 收藏/取消收藏(未登录状态跳转登陆界面)
        - 播放视频

- 收藏夹

  - 未登录状态
    
    - 去登录
    
  - 登录状态

    - 收藏电影信息

      - 删除收藏电影

      - 进入收藏电影详情

        - 电影信息

        - 收藏/取消收藏

        - 播放视频

          

- 我的

  - 未登录状态
    - 点击个人信息跳转登陆页面
    - 点击功能提醒登录

  - 登录状态

    - 个人信息

      - 个人资料

        - 设置或修改邮箱
          - 邮箱地址正则判断
          - 设置邮箱即设置密保

        - 设置或修改性别
        - 设置或修改简介

    - 播放记录
      - 播放电影列表
        - 删除播放记录
        - 电影详情
          - 电影信息
          - 收藏/取消收藏
          - 播放视频

    - 设置
      - 修改密码
        - 修改成功重新登录
      - 找回密码
        - 找回密码重置为000000
      - 设置密保
        - 邮箱地址正则判断
        - 设置密保主要方式

    - 退出登录















