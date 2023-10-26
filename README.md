# DocViewer
android DocViewer support view word excel ppt pdf txt image in sdcard & uri & assets & url

### Rendering
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/rendering.gif" width = "275" height = "569"  /><br>

# Screenshot
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_1.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_2.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_3.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_4.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_5.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_6.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_7.png" width = "275" height = "569"  /><br>
<img src="https://github.com/Victor2018/DocViewer/blob/main/SrceenShot/st_8.png" width = "275" height = "569"  /><br>

### Usage
- Step 1. Add the JitPack repository to your build or setting file

```
build file:
allprojects {
    repositories {
        ...
     maven { url "https://jitpack.io" }
    }
}

or setting file:
dependencyResolutionManagement {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
- Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.Victor2018:DocViewer:latestVersion'
}
```

- Step 3.  set layoutManager for recyclerView by kotlin
```kotlin
DocViewerActivity.launchDocViewer(this,docSourceType,path)

```

# 关注开发者：
- 邮箱： victor423099@gmail.com
- 新浪微博
- ![image](https://github.com/Victor2018/AppUpdateLib/raw/master/SrceenShot/sina_weibo.jpg)

### 感谢
https://github.com/vantinh689/AllDocumentViewerNew

## License

Copyright (c) 2023 Victor

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

