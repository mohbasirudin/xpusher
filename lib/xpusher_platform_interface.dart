import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'xpusher_method_channel.dart';

abstract class XpusherPlatform extends PlatformInterface {
  /// Constructs a XpusherPlatform.
  XpusherPlatform() : super(token: _token);

  static final Object _token = Object();

  static XpusherPlatform _instance = MethodChannelXpusher();

  /// The default instance of [XpusherPlatform] to use.
  ///
  /// Defaults to [MethodChannelXpusher].
  static XpusherPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [XpusherPlatform] when
  /// they register themselves.
  static set instance(XpusherPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
