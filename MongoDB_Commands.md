# _MongoDB Commands_

* **_Show All Databases_**

```bash
show dbs
```

* **_Show Current Database_**

```bash
db
```

* **_Create Or Switch Database_**

```bash
use test
```

* **_Drop_**

```bash
db.dropDatabase()
```

* **_Create Collection_**

```bash
db.createCollection('posts')
```

* **_Show Collections_**

```bash
show collections
```

* **_Insert Row_**

```bash
db.posts.insert({
  title: 'Post One',
  body: 'Body of post one',
  category: 'News',
  tags: ['news', 'events'],
  user: {
    name: 'John Doe',
    status: 'author'
  },
  date: Date()
})
```

* **_Insert Multiple Rows_**

```bash
db.posts.insertMany([
  {
    title: 'Post Two',
    body: 'Body of post two',
    category: 'Technology',
    date: Date()
  },
  {
    title: 'Post Three',
    body: 'Body of post three',
    category: 'News',
    date: Date()
  },
  {
    title: 'Post Four',
    body: 'Body of post three',
    category: 'Entertainment',
    date: Date()
  }
])
```

* **_Get All Rows_**

```bash
db.posts.find()
```

* **_Get All Rows Formatted_**

```bash
db.posts.find().pretty()
```

* **_Find Rows_**

```bash
db.posts.find({ category: 'News' })
```

* **_Sort Rows_**

```bash
# Ascending
db.posts.find().sort({ title: 1 }).pretty()
# Descending
db.posts.find().sort({ title: -1 }).pretty()
```

* **_Count Rows_**

```bash
db.posts.find().count()
db.posts.find({ category: 'news' }).count()
```

* **_Limit Rows_**

```bash
db.posts.find().limit(2).pretty()
```

* **_Chaining_**

```bash
db.posts.find().limit(2).sort({ title: 1 }).pretty()
```

* **_Foreach_**

```bash
db.posts.find().forEach(function(doc) {
  print("Blog Post: " + doc.title)
})
```

* **_Find One Row_**

```bash
db.posts.findOne({ category: 'News' })
```

* **_Find Specific Fields_**

```bash
db.posts.find({ title: 'Post One' }, {
  title: 1,
  author: 1
})
```

* **_Update Row_**

```bash
db.posts.update({ title: 'Post Two' },
{
  title: 'Post Two',
  body: 'New body for post 2',
  date: Date()
},
{
  upsert: true
})
```

* **_Update Specific Field_**

```bash
db.posts.update({ title: 'Post Two' },
{
  $set: {
    body: 'Body for post 2',
    category: 'Technology'
  }
})
```

* **_Increment Field ($inc)_**

```bash
db.posts.update({ title: 'Post Two' },
{
  $inc: {
    likes: 5
  }
})
```

* **_Rename Field_**

```bash
db.posts.update({ title: 'Post Two' },
{
  $rename: {
    likes: 'views'
  }
})
```

* **_Delete Row_**

```bash
db.posts.remove({ title: 'Post Four' })
```

* **_Sub-Documents_**

```bash
db.posts.update({ title: 'Post One' },
{
  $set: {
    comments: [
      {
        body: 'Comment One',
        user: 'Mary Williams',
        date: Date()
      },
      {
        body: 'Comment Two',
        user: 'Harry White',
        date: Date()
      }
    ]
  }
})
```

* **_Find By Element in Array ($elemMatch)_**

```bash
db.posts.find({
  comments: {
     $elemMatch: {
       user: 'Mary Williams'
       }
    }
  }
)
```

* **_Add Index_**

```bash
db.posts.createIndex({ title: 'text' })
```

* **_Text Search_**

```bash
db.posts.find({
  $text: {
    $search: "\"Post O\""
    }
})
```

* **_Greater & Less Than_**

```bash
db.posts.find({ views: { $gt: 2 } })
db.posts.find({ views: { $gte: 7 } })
db.posts.find({ views: { $lt: 7 } })
db.posts.find({ views: { $lte: 7 } })
```
