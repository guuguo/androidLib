package com.guuguo.android.lib.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.guuguo.android.lib.BaseApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by mimi on 2017-01-19.
 */

public class NetWorkUtil {
    /**
     * The constant UNKNOWN.
     */
    public static final int UNKNOWN = 0;
    /**
     * The constant WIFI_WIFIMAX.
     */
    public static final int WIFI_WIFIMAX = 1;
    /**
     * The constant CELLULAR_UNKNOWN.
     */
    public static final int CELLULAR_UNKNOWN = 2;
    /**
     * The constant CELLULAR_2G.
     */
    public static final int CELLULAR_2G = 3;
    /**
     * The constant CELLULAR_3G.
     */
    public static final int CELLULAR_3G = 4;
    /**
     * The constant CELLULAR_4G.
     */
    public static final int CELLULAR_4G = 5;
    /**
     * The constant CELLULAR_UNIDENTIFIED_GEN.
     */
    public static final int CELLULAR_UNIDENTIFIED_GEN = 6;
    /**
     * The constant SOCKET_EXCEPTION.
     */
    public static final String SOCKET_EXCEPTION = "Socket Exception";

    /**
     * Instantiates a new Easy  network mod.
     *
     * @param context
     *     the context
     */

    /**
     * Is wifi enabled.
     *
     * @return the boolean
     */
    public static final boolean isWifiEnabled() {
        boolean wifiState = false;

        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiState = wifiManager.isWifiEnabled();
        }
        return wifiState;
    }

    /**
     * Is network available boolean.
     *
     * @return the boolean
     */
    @SuppressWarnings("MissingPermission")
    public static final boolean isNetworkAvailable() {

        if (CommonUtil.hasPermission(BaseApplication.getInstance(), Manifest.permission.INTERNET)
                && CommonUtil.hasPermission(BaseApplication.getInstance(), Manifest.permission.ACCESS_NETWORK_STATE)) {
            ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInstance().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;
    }

    /**
     * Gets ip address v4.
     *
     * @return the ip address
     */
    public static final String getIPv4Address() {
        String result = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (isIPv4) {
                            result = sAddr;
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return CommonUtil.checkValidData(result);
    }

    /**
     * Gets ip address v6.
     *
     * @return the ip address
     */
    public static final String getIPv6Address() {
        String result = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (!isIPv4) {
                            int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                            result = delim < 0 ? sAddr : sAddr.substring(0, delim);
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return CommonUtil.checkValidData(result);
    }

    /**
     * Gets network type.
     *
     * @return the network type
     */
    @SuppressWarnings("MissingPermission")
    public static final int getNetworkType() {
        int result = UNKNOWN;
        if (CommonUtil.hasPermission(BaseApplication.getInstance(), Manifest.permission.ACCESS_NETWORK_STATE)) {
            ConnectivityManager cm =
                    (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null) {
                result = UNKNOWN;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                    || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX) {
                result = WIFI_WIFIMAX;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager manager =
                        (TelephonyManager) BaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
                if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                    switch (manager.getNetworkType()) {

                        // Unknown
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            result = CELLULAR_UNKNOWN;
                            break;
                        // Cellular Data–2G
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            result = CELLULAR_2G;
                            break;
                        // Cellular Data–3G
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            result = CELLULAR_3G;
                            break;
                        // Cellular Data–4G
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            result = CELLULAR_4G;
                            break;
                        // Cellular Data–Unknown Generation
                        default:
                            result = CELLULAR_UNIDENTIFIED_GEN;
                            break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Gets wifi mac.
     *
     * @return the wifi mac
     */
    @SuppressWarnings("MissingPermission")
    public static final String getWifiMAC() {
        String result = "02:00:00:00:00:00";
        if (CommonUtil.hasPermission(BaseApplication.getInstance(), Manifest.permission.ACCESS_WIFI_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Hardware ID are restricted in Android 6+
                // https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
                Enumeration<NetworkInterface> interfaces = null;
                try {
                    interfaces = NetworkInterface.getNetworkInterfaces();
                } catch (SocketException e) {
                }
                while (interfaces != null && interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();

                    byte[] addr = new byte[0];
                    try {
                        addr = networkInterface.getHardwareAddress();
                    } catch (SocketException e) {
                    }
                    if (addr == null || addr.length == 0) {
                        continue;
                    }

                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    String mac = buf.toString();
                    String wifiInterfaceName = "wlan0";
                    result = wifiInterfaceName.equals(networkInterface.getName()) ? mac : result;
                }
            } else {
                WifiManager wm = (WifiManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                result = wm.getConnectionInfo().getMacAddress();
            }
        }
        return CommonUtil.checkValidData(result);
    }
}
