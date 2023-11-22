import 'package:flutter_test/flutter_test.dart';
import 'package:xpusher/xpusher.dart';
import 'package:xpusher/xpusher_platform_interface.dart';
import 'package:xpusher/xpusher_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockXpusherPlatform
    with MockPlatformInterfaceMixin
    implements XpusherPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  // final XpusherPlatform initialPlatform = XpusherPlatform.instance;
  //
  // test('$MethodChannelXpusher is the default instance', () {
  //   expect(initialPlatform, isInstanceOf<MethodChannelXpusher>());
  // });
  //
  // test('getPlatformVersion', () async {
  //   XPusher xpusherPlugin = XPusher();
  //   MockXpusherPlatform fakePlatform = MockXpusherPlatform();
  //   XpusherPlatform.instance = fakePlatform;
  //
  //   expect(await xpusherPlugin.getPlatformVersion(), '42');
  // });
}
