import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'xpusher_platform_interface.dart';

/// An implementation of [XpusherPlatform] that uses method channels.
class MethodChannelXpusher extends XpusherPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('xpusher');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
