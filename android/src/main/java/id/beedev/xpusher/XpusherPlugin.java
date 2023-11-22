package id.beedev.xpusher;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpChannelAuthorizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * XpusherPlugin
 */
public class XpusherPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Activity activity;
    private Pusher pusher;
    private String CHANNEL = "beedev/xpusher";
    private String TAG = CHANNEL;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), this.CHANNEL);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        this.activity = null;
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.activity = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        String method = call.method;
        switch (method) {
            case "init":
                init(call, result);
                break;
            case "connect":
                connect(call, result);
                break;
            case "disconnect":
                disconnect(call, result);
                break;
            case "subscribe":
//                subscribe(call);
                break;
            case "unsubscribe":
                unsubscribe(call);
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private void init(MethodCall call, Result result) {
        try {
            String apiKey = call.argument("apiKey");
            String cluster = call.argument("cluster");
            String channelName = call.argument("channelName");
            String eventName = call.argument("eventName");
            boolean isPrivate = channelName.toLowerCase().contains("private-");

            PusherOptions options = new PusherOptions();
            options.setCluster(cluster);


            if (isPrivate) {
                String host = call.argument("host");
                String endPoint = call.argument("endPoint");
                Integer wssPort = call.argument("wssPort");
                HashMap<String, String> auths = call.argument("auths");
                HttpChannelAuthorizer authorizer = new HttpChannelAuthorizer(endPoint);
                authorizer.setHeaders(auths);
                options.setChannelAuthorizer(authorizer);
                options.setWssPort(wssPort);
                options.setHost(host);
            }

            pusher = new Pusher(apiKey, options);

            connect(call, result);

            subscribe(isPrivate, channelName, eventName);
        } catch (Exception e) {
            print("[INIT] " + e);
        }
    }

    private void subscribe(Boolean isPrivate, String channelName, String eventName) {

        try {
            if (pusher == null) {
                return;
            }

//            String channelName = call.argument("channelName");
//            String eventName = call.argument("eventName");
//            boolean isPrivate = channelName.toLowerCase().contains("private-");
//

            if (isPrivate) {
                pusher.subscribePrivate(channelName, _privateChannelEventListener(), eventName);
            } else {
                pusher.subscribe(channelName).bind(eventName, _channelEventListener());
            }
        } catch (Exception e) {
            print("[SUBSCRUPTION] " + e);
        }
    }

    private void unsubscribe(MethodCall call) {
        try {
            if (pusher == null) {
                return;
            }

            String channelName = call.argument("channelName");
            pusher.unsubscribe(channelName);

        } catch (Exception e) {

        }
    }

    private ChannelEventListener _channelEventListener() {
        return new ChannelEventListener() {

            @Override
            public void onEvent(PusherEvent event) {
                print("onEvent: " + event.getData());
                callback("onEvent", event.getData());
            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {
                print("success " + channelName);
                callback("onSubscription", null);
            }
        };
    }

    private PrivateChannelEventListener _privateChannelEventListener() {
        return new PrivateChannelEventListener() {
            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                print("[PRIVATE] Failed");
                callback("onSubscription", false);
            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {
                print("[PRIVATE] Success");
                callback("onSubscription", true);
            }

            @Override
            public void onEvent(PusherEvent event) {
                callback("onEvent", event.getData());
            }
        };
    }

    private void connect(MethodCall call, Result result) {
        try {
            if (pusher == null) {
                return;
            }

            String method = "onConnectionState";

            pusher.connect(new ConnectionEventListener() {
                @Override
                public void onConnectionStateChange(ConnectionStateChange change) {
                    HashMap map = new HashMap();
                    String cState = change.getCurrentState().toString();
                    String pState = change.getPreviousState().toString();
                    map.put("currentState", cState);
                    map.put("previousState", pState);
                    print("cs: " + cState + ", ps: " + pState);

                    callback(method, map);
                }

                @Override
                public void onError(String message, String code, Exception e) {
                    callback(method, null);
                }
            }, ConnectionState.ALL);
        } catch (Exception e) {
            print("[CONNECT] " + e);
        }
    }

    private void disconnect(MethodCall call, Result result) {
        try {
            if (pusher == null) {
                return;
            }

            pusher.disconnect();
        } catch (Exception e) {
            print("[DISCONNECT] " + e);
        }
    }

    private void callback(String method, Object args) {
        try {
            activity.runOnUiThread(() -> channel.invokeMethod(method, args));
        } catch (Exception e) {
            print("[C] " + e);
        }
    }

    private void print(String message) {
        Log.d(TAG, message);
//        System.out.println(TAG + " : " + message);
    }

    private static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    private static Map<String, Object> toMap(JSONObject json) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = json.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = json.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
