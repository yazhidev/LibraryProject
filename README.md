# LibraryProject

## 版本更新历史

### 0.0.3

- 增加可自定义圆角的 TextView (参考[FlycoRoundView](https://github.com/H07000223/FlycoRoundView))
- 增加 BitmapUtil、CalcUtil

### 0.0.7 

- 增加 LoadingTextView ，在 RoundRelativelayout 的基础上添加了 loading 状态和倒计时状态

![LoadingTextView](http://upload-images.jianshu.io/upload_images/1929170-276c9468a8c79928.gif?imageMogr2/auto-orient/strip)

- 修复 RoundViewDelegate 中 mCornerRadius_BR 和 mCornerRadius_BL 颠倒的问题


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
|getFileMD5 | 获得文件MD5值|


### StatusUtil

|方法名 | 作用 |
|----|------|
|isKeyBoardShowing | 获得软键盘是否调起|
