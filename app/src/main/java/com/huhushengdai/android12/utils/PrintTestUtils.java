package com.huhushengdai.android12.utils;

import android.Manifest;
import android.content.AttributionSource;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.huhushengdai.tool.log.LogTool;

import java.lang.reflect.Method;
import java.util.Set;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

/**
 * @author xuxing
 * date：2021/11/16 9:53 上午
 * description：
 * 打印测试数据
 */
public class PrintTestUtils {

    public static void printPermissionInfo(Context context) {
        StringBuilder sb = new StringBuilder(128);
        sb.append("\n").append("target version = ").append(context.getApplicationInfo().targetSdkVersion);
        sb.append("\n").append("android.permission.BLUETOOTH = ").append(hasP(context, "android.permission.BLUETOOTH"));
        sb.append("\n").append("android.permission.BLUETOOTH_ADMIN = ").append(hasP(context, "android.permission.BLUETOOTH_ADMIN"));
        sb.append("\n").append("android.permission.ACCESS_FINE_LOCATION = ").append(hasP(context, "android.permission.ACCESS_FINE_LOCATION"));
        sb.append("\n").append("android.permission.ACCESS_COARSE_LOCATION = ").append(hasP(context, "android.permission.ACCESS_COARSE_LOCATION"));
        sb.append("\n").append("android.permission.BLUETOOTH_SCAN = ").append(hasP(context, "android.permission.BLUETOOTH_SCAN"));
        sb.append("\n").append("android.permission.BLUETOOTH_CONNECT = ").append(hasP(context, "android.permission.BLUETOOTH_CONNECT"));
        sb.append("\n").append("android.permission.BLUETOOTH_ADVERTISE = ").append(hasP(context, "android.permission.BLUETOOTH_ADVERTISE"));
        if (Build.VERSION.SDK_INT >= 31) {
            sb.append("\n").append("hasDisavowedLocationForScan = ").append(hasDisavowedLocationForScan(context, context.getAttributionSource(), false));
        } else {
            sb.append("\n").append("当前版本小于Android 31，无法查看 hasDisavowedLocationForScan");
        }
        LogTool.i("PermissionInfo:"+sb.toString());
    }

    @RequiresApi(api = 31)
    public static Set<String> getSet(AttributionSource source) {
        try {
            Method getDeclaredMethod = AttributionSource.class.getDeclaredMethod("getRenouncedPermissions");
            return (Set<String>) getDeclaredMethod.invoke(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = 31)
    public static boolean hasDisavowedLocationForScan(
            Context context, AttributionSource attributionSource, boolean inTestMode) {

        // Check every step along the attribution chain for a renouncement.
        // If location has been renounced anywhere in the chain we treat it as a disavowal.
        AttributionSource currentAttrib = attributionSource;
        while (true) {
            if (getSet(currentAttrib).contains(Manifest.permission.ACCESS_FINE_LOCATION)
                    && (inTestMode || context.checkPermission("android.permission.RENOUNCE_PERMISSIONS", -1,
                    currentAttrib.getUid())
                    == PackageManager.PERMISSION_GRANTED)) {
                return true;
            }
            AttributionSource nextAttrib = currentAttrib.getNext();
            if (nextAttrib == null) {
                break;
            }
            currentAttrib = nextAttrib;
        }

        // Check the last attribution in the chain for a neverForLocation disavowal.
        String packageName = currentAttrib.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            // TODO(b/183478032): Cache PackageInfo for use here.
            int flag = 0x00001000;
            PackageInfo pkgInfo = pm.getPackageInfo(packageName, flag);
            for (int i = 0; i < pkgInfo.requestedPermissions.length; i++) {
                if (pkgInfo.requestedPermissions[i].equals("android.permission.BLUETOOTH_SCAN")) {
                    return (pkgInfo.requestedPermissionsFlags[i]
                            & PackageInfo.REQUESTED_PERMISSION_NEVER_FOR_LOCATION) != 0;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogTool.w("Could not find package for disavowal check: " + packageName);
        }
        return false;
    }

    private static boolean hasP(Context context, String p) {
        return ContextCompat.checkSelfPermission(context, p) == 0;
    }
}
