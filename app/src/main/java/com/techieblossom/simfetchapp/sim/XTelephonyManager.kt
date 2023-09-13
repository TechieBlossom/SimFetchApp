package co.tide.universal.carrier

import android.telephony.TelephonyManager

fun TelephonyManager.isSimActive() = when (simState) {
    TelephonyManager.SIM_STATE_READY -> true
    else -> false
}