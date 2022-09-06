package org.proninyaroslav.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import dev.zacsweers.moshix.sealed.annotations.NestedSealed
import dev.zacsweers.moshix.sealed.annotations.TypeLabel

class MainActivity : AppCompatActivity() {
    val moshi = Moshi.Builder()
        .add(NestedSealed.Factory())
        .build()
    val adapter = moshi.adapter(BundleType::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = adapter.toJson(BundleType(bar = Foo.Bar.Variant1(1)))
        Log.i("", "Serialized: $json")

        val value = adapter.fromJson(json)
        Log.i("", "Deserialized: $value")
    }
}

@JsonClass(generateAdapter = true)
data class BundleType(val bar: Foo.Bar)

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed interface Foo {
    @NestedSealed
    sealed interface Bar : Foo {
        @TypeLabel("bar_unknown")
        object Unknown : Bar

        @TypeLabel("bar_variant1")
        @JsonClass(generateAdapter = true)
        data class Variant1(val number: Int) : Bar

        @TypeLabel("bar_variant2")
        @JsonClass(generateAdapter = true)
        data class Variant2(val string: String) : Bar
    }
}