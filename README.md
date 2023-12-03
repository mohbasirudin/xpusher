# XPusher

XPusher is unofficial plugin pusher for Flutter.
- On Android it was tested on Android-SDK 34
- On iOS : Maintenance

## Supported Platform
- Android
- iOS : Maintenance

## Installation

Add to your pubspec.yaml

```yaml
dependencies:
    xpusher: 
```

## Subscription to Channels
### Public Channel
```dart
String channel = "my-channel";
```

### Private Channel
```dart
String channel = "private-my-channel";
```

## API Overview

### Initialization

```dart
XPusher pusher = XPusher();
```

### Public Channel
```dart
  pusher.init(
    apiKey: "YOUR_API_KEY",
    cluster: "YOUR_CLUSTER",
    channelName: "YOUR_CHANNEL_NAME",
    eventName: "YOUT_EVENT_NAME",
    onConnectionState: (currentState, previousState) {
      print("state: $currentState, $previousState");
    },
    onEvent: (data) {
      print("data: $data");
    },
  );
```
### Private Channel
```dart
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

## Callback Function

## onConnectionState
`onConnectionState` to get status of your connections.
```dart
    onConnectionState: (currentState, previousState) {
      print("state: $currentState, $previousState");
    },
```

| Status       | Description        |
|--------------|--------------------|
| `CONNECTED`  | Successful         |
| `DISCONNECT` | Failed             |
| `CONNECTING` | Process to Connect |

## onEvent
`onEvent` it will give you a `dynamic` result
```dart
    onEvent: (data) {
      print("data: $data");
    },
```

