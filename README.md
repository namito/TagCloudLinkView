TagCloudLinkView
================

A Simple Layout with Tag View.

It provides add and remove Tag view.
(Tag object is packaged. 'com.ns.developer.tagview.entity.Tag')

It also can callback a selected tag text and deletable tag text ('×') .

Installing
----

###Gradle
```java
dependencies {
    compile 'com.ns-developer:tagcloudview:0.1.0'
}
```

Usage
-----
# layout in xml

# add tag object

at Activity.
```java
TagCloudLinkView view = (TagCloudLinkView) findViewById(R.id.xxx);
view.add(new Tag(1,"TAG TEXT 1"));
view.add(new Tag(1,"TAG TEXT 2"));
view.add(new Tag(1,"TAG TEXT 3"));
view.drawTags();
```

License
=======

    Copyright 2014 Namito.S

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and



