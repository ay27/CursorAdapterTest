##一个简单的Demo


###使用了ContentProvider+LoaderManager+CursorAdapter，为ListView提供数据

###为什么要这样做
1. 传统的ListVIew更新数据操作麻烦，且在不同的线程中会产生问题
2. 使用ContentProvider可以简化、结构化数据操作
3. 通过ContentProvider作为中介，可以很方便的在不同的线程中更新ListView，特别是在网络应用方面

###受到[stormzhang](http://stormzhang.github.io)的[9GAG](https://github.com/stormzhang/9GAG)项目的启发
在这个项目里边我知道了REST Client这个概念，知道了原来网络应用可以通过数据库进行数据中转，从而巧妙的解决了网络不能在UI线程中的问题，而且通过 ` CursorAdapter ` 可以很方便的与数据库进行绑定。


