##一个简单的Demo


###使用了ContentProvider+LoaderManager+CursorAdapter，为ListView提供数据

###为什么要这样做
1. 传统的ListVIew更新数据操作麻烦，且在不同的线程中会产生问题
2. 使用ContentProvider可以简化、结构化数据操作
3. 通过ContentProvider作为中介，可以很方便的在不同的线程中更新ListView，特别是在网络应用方面

###受到[stormzhang](http://stormzhang.github.io)的[9GAG](https://github.com/stormzhang/9GAG)项目的启发
在这个项目里边我知道了REST Client这个概念，知道了原来网络应用可以通过数据库进行数据中转，从而巧妙的解决了网络不能在UI线程中的问题，而且通过 ` CursorAdapter ` 可以很方便的与数据库进行绑定。
当然我这个demo只是练手而已，尚不涉及网络部分。

###简单解释
1. [ContentProvider](http://developer.android.com/guide/topics/providers/content-providers.html)作为数据提供者，主要是封装数据库的操作
2. [LoaderManager](http://developer.android.com/guide/components/loaders.html)主要是管理各种loader，使其异步加载数据，避免产生性能问题
3. [CursorAdapter](http://developer.android.com/reference/android/widget/CursorAdapter.html)作为ListView的Adapter，可以直接绑定到ContentProvider，使得数据与数据库完全同步
4. DataBaseHelper继承自[SQLiteOpenHelper](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html)，主要用于数据库的创建和更新

###难点
从完全不懂ContentProvider到写出完整的Demo，花了大概一天的时间，主要是卡在了对数据库进行了Insert或者Delete操作后，不能实时反应到ListView上。各种找资料各种蛋疼，主要都是说以下几种方法：

1. 在LoaderManager的Callback中的OnLoadFinished函数中 ` adapter.swapCursor(cursor) `，经验证，不太靠谱，原因是LoaderManager的Callback只在Activity启动时会触发，即使在ContentProvider的insert操作完成后 ` getContext().getContentResolver().notifyChange(uri, null) `
2. 在更新数据后，强制刷新LoaderManager： ` getLoaderManager().restartLoader(0, null, this) `，实测可用，但是担心性能问题
3. 更新数据后重新query获取新的Cursor，然后替换掉adapter中的Cursor，实测可用，但是性能估计还是不好
4. 应该还有更好的方法，寻求中。。。
