package com.example.ass2cloud.Models

import android.os.Parcel
import android.os.Parcelable

class PrdouctsModel(var id: String?, var product_img_model:String?, var product_text_model:String?) :
 Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor():this("","","")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(product_img_model)
        parcel.writeString(product_text_model)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrdouctsModel> {
        override fun createFromParcel(parcel: Parcel): PrdouctsModel {
            return PrdouctsModel(parcel)
        }

        override fun newArray(size: Int): Array<PrdouctsModel?> {
            return arrayOfNulls(size)
        }
    }

}