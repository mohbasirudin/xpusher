import 'package:flutter/material.dart';
import 'package:xpusher/xpusher.dart';

class PageHome extends StatefulWidget {
  const PageHome({super.key});

  @override
  State<PageHome> createState() => _PageHomeState();
}

class _PageHomeState extends State<PageHome> {
  final TextEditingController _conApiKey = TextEditingController();
  final TextEditingController _conCluster = TextEditingController();
  final TextEditingController _conChannelName = TextEditingController();
  final TextEditingController _conEventName = TextEditingController();

  String currentState = "-";
  String previousState = "-";
  String currentData = "-";

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    init();
  }

  init() {
    _conApiKey.text = "d23b3083a291355fa2bc";
    // _conApiKey.text = "aab7cc27-552a-43c8-8bd2-c1f771ec4fd2";
    _conChannelName.text = "cName";
    _conEventName.text = "eName";
    _conCluster.text = "ap1";
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();

    _conApiKey.dispose();
    _conChannelName.dispose();
    _conEventName.dispose();
    _conCluster.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("XPusher"),
      ),
      body: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            margin: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              border: Border.all(),
            ),
            child: Column(
              children: [
                Row(
                  children: [
                    _status(name: "Current", value: currentState),
                    _status(name: "Previous", value: previousState),
                  ],
                ),
                const Divider(
                  thickness: 2,
                  height: 24,
                ),
                Text("Current data: $currentData"),
              ],
            ),
          ),
          Expanded(
            child: ListView(
              padding: const EdgeInsets.all(
                12,
              ),
              children: [
                _input(label: "Api Key", controller: _conApiKey),
                _input(label: "Channel Name", controller: _conChannelName),
                _input(label: "Event Name", controller: _conEventName),
                _input(label: "Cluster", controller: _conCluster),
              ],
            ),
          ),
        ],
      ),
      bottomNavigationBar: BottomAppBar(
        padding: const EdgeInsets.symmetric(
          horizontal: 12,
          vertical: 6,
        ),
        child: SizedBox(
          height: 40,
          child: ElevatedButton(
            onPressed: () {
              _connect();
            },
            child: const Text("Connect"),
          ),
        ),
      ),
    );
  }

  void _connect() async {
    XPusher pusher = XPusher();
    pusher.init(
      apiKey: _conApiKey.text,
      cluster: _conCluster.text,
      channelName: _conChannelName.text,
      eventName: _conEventName.text,
      onConnectionState: (currentState, previousState) {
        this.currentState = currentState;
        this.previousState = previousState;
        setState(() {});
      },
      onEvent: (data) {
        currentData = data;
        setState(() {});
      },
    );
  }

  Widget _status({
    required String name,
    required String value,
  }) {
    return Expanded(
      child: Column(
        children: [
          Text(
            name,
            style: const TextStyle(
              fontWeight: FontWeight.w300,
            ),
          ),
          Text(
            value,
            style: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 16,
            ),
          ),
        ],
      ),
    );
  }

  Widget _input({
    required String label,
    required TextEditingController controller,
    TextInputType? textInputType,
  }) {
    return Padding(
      padding: const EdgeInsets.only(
        bottom: 12,
      ),
      child: TextField(
        controller: controller,
        keyboardType: textInputType,
        decoration: InputDecoration(
          label: Text(label),
          border: const OutlineInputBorder(),
        ),
      ),
    );
  }
}
