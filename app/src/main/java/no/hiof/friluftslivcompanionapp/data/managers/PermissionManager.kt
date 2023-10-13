package no.hiof.friluftslivcompanionapp.data.managers

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Manages permission-related operations, specifically for requesting a single permission.
 *
 * This class simplifies the process of requesting permissions by providing a callback-based approach.
 *
 * @param registry The [ActivityResultRegistry], used to register the permission request.
 * @param onPermissionGranted A callback that gets triggered when the permission is granted.
 * @param onPermissionDenied A callback that gets triggered when the permission is denied.
 *
 * @property requestPermissionLauncher The launcher used to request the permission and handle the result.
 */
class PermissionManager(
    registry: ActivityResultRegistry,
    private val onPermissionGranted: () -> Unit,
    private val onPermissionDenied: () -> Unit
) {

    private val requestPermissionLauncher =
        registry.register("permissionRequest", ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    fun requestPermission(permission: String) {
        requestPermissionLauncher.launch(permission)
    }
}