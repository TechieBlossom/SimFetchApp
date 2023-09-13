package com.techieblossom.simfetchapp.sim

data class SimInfo(
    val subscriptionId: Int,
    val subscriptionType: Int?,
    val phoneNumber: String,
    val isSimActive: Boolean,
    val simSlotIndex: Int,
    val carrierName: String?,
)

fun List<SimInfo>.toJsonList(): List<String> {
    val jsonList = this.map {
        """{
                "subscriptionId": ${it.subscriptionId},
                "subscriptionType": ${it.subscriptionType},
                "phoneNumber": "${it.phoneNumber}",
                "isSimActive": ${it.isSimActive},
                "simSlotIndex": ${it.simSlotIndex},
                "carrierName": "${it.carrierName}"
           }
        """.trimMargin()
    }
    return jsonList
}

