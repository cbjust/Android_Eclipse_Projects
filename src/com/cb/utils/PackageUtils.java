package com.cb.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

/** PackageManager相关工具类 */
public class PackageUtils
{
    /** 版本号name */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString(), e);
        }
        return "";
    }

    /** 版本号code */
    public static int getVersionCode(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        }
        catch (Exception e)
        {
            LogUtils.error(e.toString(), e);
        }
        return -1;
    }
}
