package com.example.parentallauncher

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class DeviceOwnerRequest : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val compName = ComponentName(this, MyDeviceAdminReceiver::class.java)

        if (!dpm.isAdminActive(compName)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Pour activer le contrôle parental, autorisez cette app comme administrateur.")
            startActivityForResult(intent, 1)
        } else if (!dpm.isDeviceOwnerApp(packageName)) {
            Toast.makeText(this, "Cette application doit être définie en tant que Device Owner via ADB.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Device Owner activé !", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Device Admin activé, veuillez définir l'app en device owner via ADB.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Activation Device Admin annulée.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
