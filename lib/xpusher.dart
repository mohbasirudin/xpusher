import 'package:flutter/services.dart';
import 'package:xpusher/keys.dart';
import 'package:xpusher/method.dart';

class XPusher {
  final MethodChannel _mc = const MethodChannel("beedev/xpusher");

  Function(String currentState, String previousState)? onConnectionState;
  Function(dynamic data)? onEvent;

  Future<void> init({
    required String apiKey,
    String? cluster,
    String? host,
    String? endPoint,
    int? wssPort,
    Map<String, String>? auths,
    Function(String currentState, String previousState)? onConnectionState,
    required String channelName,
    String? eventName,
    Function(dynamic data)? onEvent,
  }) async {
    _mc.setMethodCallHandler(_setMethodCallHandler);

    this.onConnectionState = onConnectionState;
    this.onEvent = onEvent;

    await _mc.invokeMethod(
      Method.init,
      {
        Keys.apiKey: apiKey,
        Keys.cluster: cluster,
        Keys.host: host,
        Keys.auths: auths,
        Keys.endPoint: endPoint,
        Keys.wssPort: wssPort ?? 6002,
        Keys.channelName: channelName,
        Keys.eventName: eventName ?? "",
      },
    );
  }

  Future<void> disconnect({
    required String channelName,
  }) async {
    await _mc.invokeMethod(Method.disconnect, {
      Keys.channelName: channelName,
    });
  }

  Future<dynamic> _setMethodCallHandler(MethodCall call) {
    String method = call.method;
    switch (method) {
      case Method.onConnectionState:
        Map map = call.arguments;
        onConnectionState!.call(
          map[Keys.currentState],
          map[Keys.previousState],
        );
        return Future.value(null);
      case Method.onEvent:
        onEvent!.call(call.arguments);
        return Future.value(null);
      default:
        throw MissingPluginException('Unknown method ${call.method}');
    }
  }
}
