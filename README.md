# XPusher

## Supported Platform
- Android : OK
- iOS : Maintenance

## Initializing
```dart
XPusher pusher = XPusher();

  pusher.init(
    apiKey: "YOUR_API_KEY",
    cluster: "YOUR_CLUSTER",
    channelName: "YOUR_CHANNEL_NAME",
    eventName: "YOUT_EVENT_NAME",
    auths: "", // default = {}
    endPoint: "YOUR_END_POINT_AUTH",
    host: "pushnow.my.id", // default = "pushnow.my.id"
    wssPort: 6002, // default = 6002
    onConnectionState: (currentState, previousState) {
      print("state: $currentState, $previousState");
    },
    onEvent: (data) {
      print("data: $data");
    },
  );
```

