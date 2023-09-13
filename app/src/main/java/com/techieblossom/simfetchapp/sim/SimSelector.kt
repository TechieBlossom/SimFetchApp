package com.techieblossom.simfetchapp.sim

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import co.tide.universal.carrier.SimInfo
import co.tide.universal.carrier.isSimActive

internal class SimSelector(private val context: Context) {

    private var defaultTelephonyManager: TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private var subscriptionManager: SubscriptionManager =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    fun getInfo(): List<SimInfo?>? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, CarrierException.PermissionDeniedException.toString())
        } else {
            val simInfoList = subscriptionManager.activeSubscriptionInfoList?.map {
                try {
                    val telephonyManager =
                        defaultTelephonyManager.createForSubscriptionId(it.subscriptionId)
                    val simInfo = SimInfo(
                        subscriptionId = it.subscriptionId,
                        subscriptionType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) it.subscriptionType else null,
                        phoneNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            subscriptionManager.getPhoneNumber(it.subscriptionId) else it.number,
                        isSimActive = telephonyManager.isSimActive(),
                        simSlotIndex = it.simSlotIndex,
                        carrierName = telephonyManager?.simOperatorName
                            ?: it.carrierName?.toString()
                    )
                    simInfo
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    null
                }
            }
            return simInfoList
        }
        return null
    }
}
