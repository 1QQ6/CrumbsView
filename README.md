# CrumbsView
Android端实现面包屑UI效果+Fragment联动

#面包屑UI

<img src="https://github.com/1QQ6/CrumbsView/blob/master/GIF/device-2021-07-11-210616.gif" width=480 height=1080/>

#加入依赖：
```
    implementation 'com.github.1QQ6:CrumbsView:v1.0.4'
```
#使用方法：
```
<com.example.crumbsview.StrongCrumbsView
        android:id="@+id/breadCrumbs"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:background="@color/white"
        android:paddingStart="24dp"
        android:paddingEnd="24dp"
        android:visibility="visible"
        app:select_item_color="@color/color_8A000000"
        app:unselected_item_color="@color/black"
        app:layout_constraintTop_toBottomOf="@+id/header" />
```
