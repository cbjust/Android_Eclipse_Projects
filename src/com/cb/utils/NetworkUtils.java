package com.cb.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * Network-related
 */
public class NetworkUtils
{
    /**
     * Returns whether the network is available
     * 
     * @param context Context
     * @return 网络是否可用
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNetworkAvailable(Context context)
    {
        return getConnectedNetworkInfo(context) != null;
    }

    /**
     * 获取网络类型
     * 
     * @param context Context
     * @return 网络类型
     * @see [类、类#方法、类#成员]
     */
    public static int getNetworkType(Context context)
    {
        NetworkInfo networkInfo = getConnectedNetworkInfo(context);
        if (networkInfo != null)
        {
            return networkInfo.getType();
        }

        return -1;
    }

    public static NetworkInfo getConnectedNetworkInfo(Context context)
    {
        try
        {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null)
            {
                LogUtils.error("couldn't get connectivity manager");
            }
            else
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                {
                    for (int i = 0; i < info.length; i++)
                    {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return info[i];
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            LogUtils.error(e.toString(), e);
        }
        return null;
    }

    /**
     * 判断网络是不是手机网络，非wifi
     * 
     * @param context Context
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    public static boolean isMobileNetwork(Context context)
    {
        return isNetworkAvailable(context) && (ConnectivityManager.TYPE_MOBILE == getNetworkType(context));
    }

    /**
     * 判断网络是不是wifi
     * 
     * @param context Context
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    public static boolean isWifiNetwork(Context context)
    {
        return isNetworkAvailable(context) && (ConnectivityManager.TYPE_WIFI == getNetworkType(context));
    }

    /** 获取wifi名称 */
    public static String getWifiSSID(Context context)
    {
        if (isWifiNetwork(context))
        {
            try
            {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                return info.getSSID();
            }
            catch (Exception e)
            {
                LogUtils.error(e.toString(), e);
            }
        }
        return null;
    }

    /** 获取本机ip */
    public static String getLocalIpAddress()
    {
        // String ipaddress = "";
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements())
            {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements())
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    // loopback地址就是代表本机的IP地址，只要第一个字节是127，就是lookback地址
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                        // ipaddress = ipaddress + ";" +
                        // inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString(), e);
        }
        return null;
        // return ipaddress;
    }

    /** 手机号码 */
    public static String getLine1Number(Context context)
    {
        try
        {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getLine1Number();
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString(), e);
        }
        return "";
    }

    public static String getWifiMacAddress(Context context)
    {
        try
        {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString(), e);
        }
        return "";
    }

}
