# taskit-android

# React-Native环境配置

------

#### Node

安装完node后建议设置npm镜像以加速后面的过程（或使用科学上网工具）。

```
npm config set registry https://registry.npm.taobao.org --global
npm config set disturl https://npm.taobao.org/dist --global
```

#### Yarn、React Native的命令行工具（react-native-cli）

```
npm install -g yarn react-native-cli
```

安装完yarn后同理也要设置镜像源：

```
yarn config set registry https://registry.npm.taobao.org --global
yarn config set disturl https://npm.taobao.org/dist --global
```

#### Android Studio

React Native目前需要[Android Studio](http://developer.android.com/sdk/index.html)2.0或更高版本。

> Android Studio需要Java Development Kit [JDK] 1.8（暂不支持更高版本）。你可以在命令行中输入 `javac -version`来查看你当前安装的JDK版本。如果版本不合要求，则可以到 [官网](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)上下载。

需要在SDK Manager安装如下：

- 在`SDK Platforms`窗口中，选择`Show Package Details`，然后在`Android 6.0 (Marshmallow)`中勾选`Google APIs`、`Android SDK Platform 23`、`Intel x86 Atom System Image`、`Intel x86 Atom_64 System Image`以及`Google APIs Intel x86 Atom_64 System Image`。
- PS：React-Native只能在Android 6.0下进行编译

![platforms](https://reactnative.cn/static/docs/0.51/img/react-native-android-studio-android-sdk-platforms.png)

在`SDK Tools`窗口中，选择`Show Package Details`，然后在`Android SDK Build Tools`中勾选`Android SDK Build-Tools 23.0.1`（必须是这个版本）。然后还要勾选最底部的`Android Support Repository（这个一般在装Android Studio时会默认安装）`.

![build tools](https://reactnative.cn/static/docs/0.51/img/react-native-android-studio-android-sdk-build-tools.png)

#### ANDROID_HOME环境变量（如果已经有安卓环境请跳过）

确保`ANDROID_HOME`环境变量正确地指向了你安装的Android SDK的路径。具体的做法是把下面的命令加入到`~/.bash_profile`文件中：(**译注**：~表示用户目录，即`/Users/你的用户名/`。

```
# 如果你不是通过Android Studio安装的sdk，则其路径可能不同，请自行确定清楚。
export ANDROID_HOME=~/Library/Android/sdk
```

然后使用下列命令使其立即生效（否则重启后才生效）：

```
source ~/.bash_profile
```

可以使用`echo $ANDROID_HOME`检查此变量是否已正确设置。

#### Genymotion（如果有模拟器了请跳过）

比起Android Studio自带的原装模拟器，Genymotion是一个性能更好的选择，但它只对个人用户免费。

1. 下载和安装[Genymotion](https://www.genymotion.com/download)（genymotion需要依赖VirtualBox虚拟机，下载选项中提供了包含VirtualBox和不包含的选项，请按需选择）。
2. 打开Genymotion。如果你还没有安装VirtualBox，则此时会提示你安装。
3. 创建一个新模拟器并启动。
4. 启动React Native应用后，可以按下⌘+M来打开开发者菜单。

#### Gradle Daemon

开启[Gradle Daemon](https://docs.gradle.org/2.9/userguide/gradle_daemon.html)可以极大地提升java代码的增量编译速度。

```
touch ~/.gradle/gradle.properties && echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties
```

# 测试运行

------

1. 打开模拟器
2. `adb devices`确认仅有一台模拟器链接
3. 命令行定位到项目根目录
4. `react-native run-android`
5. 👌



# 参考

------

[React Native 中文网]: https://reactnative.cn/docs/0.51/getting-started.html#content	"搭建开发环境"

