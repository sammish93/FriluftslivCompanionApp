package no.hiof.friluftslivcompanionapp.data.managers

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts


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