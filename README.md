# Mikoto-Pixiv-Forward

Mikoto-Pixiv-Forward 是为了规避pixiv的反爬虫机制,同时为了应对中国特殊的网络环境,我们使用pixiv-forward进行pixiv数据的转发

我们不建议您将pixiv-forward项目部署在中国大陆地区

## 如何使用?

### Step.1

部署pixiv-forward的方法也很简单,您只需要在 [release](https://github.com/mikoto2464/pixiv/releases) 页面下载此项目的jar包 并将其下载到您的目标服务器 接着执行以下命令

```bash
nohup java -jar pixiv-forward-(version).jar [ip] [port] [userName] [password] > pixiv-forward.log 2>&1 &
```

上面命令中的

```bash
[ip] [port] [userName] [password]
```

分别为您的pixiv-web-database的 ip 端口 用户名 密码 请正确填写

我们为pixiv-forward免费提供了两个转发服务器:

```
https://pixiv-forward-1.mikoto-tech.cc
https://pixiv-forward-2.mikoto-tech.cc //Offline
https://pixiv-forward-3.mikoto-tech.cc
```

并且开放测试key: 1fc499f4ef758ad328505f6747d39198c9373cb1dfe893f21300f0eeb7a3f4c4

## 声明

### 一切开发旨在学习，请勿用于非法用途

- Mikoto-Pixiv 是完全免费且开放源代码的软件，仅供学习和娱乐用途使用
- Mikoto-Pixiv 不会通过任何方式强制收取费用，或对使用者提出物质条件
- Mikoto-Pixiv 由整个开源社区维护，并不是属于某个个体的作品，所有贡献者都享有其作品的著作权。

### 许可证

    Copyright (C) 2021-2021 Mikoto and contributors.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

`mikoto-pixiv` 采用 `AGPLv3` 协议开源。为了整个社区的良性发展，我们**强烈建议**您做到以下几点：

- **间接接触（包括但不限于使用 `Http API` 或 跨进程技术）到 `mikoto-pixiv` 的软件使用 `AGPLv3` 开源**
- **不鼓励，不支持一切商业使用**

鉴于项目的特殊性，开发团队可能在任何时间**停止更新**或**删除项目**。
