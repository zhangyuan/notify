## Hello, Notify

This app is initially built for receiving alerts and notifications.

## How to build

Notify depends on [AVOS Cloud](https://cn.avoscloud.com/) service. An app must be created in AVOS Cloud console first.

* Open the project with Android Studio.
* Copy `app/src/main/assets/app.properties.example` to `app/src/main/assets/app.properties`. Then copy the `app_id` and `app_key` from AVOS Cloud app and paste into it.
* Make the project.

## Push message data structure

Example:

```
{
  "where": {
    "channels": [
      "public"
    ]
  },
  "data": {
    "action": "com.avos.UPDATE_STATUS",
    "type": "message",
    "id": 1,
    "title": "You...",
    "content": "You may say I'm a dreamer, but I'm not the only one"
  }
}
```


## License

Notify is released under the [MIT License](http://www.opensource.org/licenses/MIT).
