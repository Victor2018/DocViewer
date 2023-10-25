package com.cherry.doc.util;

import androidx.annotation.NonNull;

public interface OnPermissionCallback {

    void onPermissionGranted(@NonNull String[] permissionName);

    void onPermissionDeclined(@NonNull String[] permissionName);

    void onPermissionPreGranted(@NonNull String permissionsName);

    void onPermissionNeedExplanation(@NonNull String permissionName);

    void onPermissionReallyDeclined(@NonNull String permissionName);

    void onNoPermissionNeeded();
}
