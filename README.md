# LibraryProject

## 版本更新历史

### 0.0.1

- 新增可自定义圆角的 TextView (参考[FlycoRoundView](https://github.com/H07000223/FlycoRoundView))
- 新增 BitmapUtil、CalcUtil

### 0.0.2 

- 新增 RoundLoadingView ，在 RoundRelativelayout 的基础上添加了 loading 状态和倒计时状态

![RoundLoadingView](http://upload-images.jianshu.io/upload_images/1929170-276c9468a8c79928.gif?imageMogr2/auto-orient/strip)

- 修复 RoundViewDelegate 中 mCornerRadius_BR 和 mCornerRadius_BL 颠倒的问题

### 0.0.3

- 新增新闻轮播控件 AutoLoopView, 后续根据需要可能会扩展类似于淘宝头条和大众点评的双行/带图片的轮播视图

![轮播视图](http://upload-images.jianshu.io/upload_images/1929170-665c66011885aafc.gif?imageMogr2/auto-orient/strip)

- 新增一款指示器和 ViewPager 滑动效果

![banner、指示器](http://upload-images.jianshu.io/upload_images/1929170-2c3b2eb649c3d1eb.gif?imageMogr2/auto-orient/strip)

- 新增 AutoEditText, 封装了隐藏软键盘时自动隐藏光标，设置最大输入字数等常用方法

### BitmapUtil

|方法名 | 作用 |
|----|------|
|getBitmap | 根据drawble文件名得到bitmap |
|saveBitmap | 保存bitmap到本地  |
|scaleBitmap | 指定尺寸缩放bitmap |

### CalcUtil

|方法名 | 作用 |
|----|------|
|dp2px | dp转化为px|
|dx2dp | px转化为dp|
|getFileMD5 | 获得文件MD5值|

### TimeUtils

|方法名 | 作用 |
|----|------|
|getDayInWeek | 根据格式化字符串时间计算周几|
|dx2dp | px转化为dp|
|dp2dx | dp转化为dx|
|sp2px | sp转化为px|
|px2sp | px转化为sp|
|getFileMD5 | 获得文件MD5值|


### PhoneUtils

|方法名 | 作用 |
|----|------|
|isPad | 是否是Pad|
|isKeyBoardShowing | 获得软键盘是否调起|
|isKeyBoardShowing | 获得软键盘是否调起|
