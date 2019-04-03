/**
 * Copyright (c) 2013, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.ksdme.lingo.knife.components

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.widget.TextView

/**
 * CustomTypefaceSpan allows for the use of a non-framework font supplied in the
 * assets/fonts directory of the application. Use this class whenever the
 * framework does not contain a needed font.
 */
class CustomTypefaceSpan
/**
 * @param family Ignored since this uses a completely custom included font.
 * @param type Typeface, specified as: Typeface.createFromAsset(
 * context.getAssets(), "fonts/Roboto-Medium.ttf"),
 * @param size Desired font size; this should have already been converted
 * from a dimension.
 * @param color Desired font color; this should have already been converted
 * to an integer representation of a color.
 */
    (family: String, private val newType: Typeface, private val newSize: Int, private val newColor: Int) :
    TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType, newSize, newColor)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType, newSize, newColor)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface, newSize: Int, newColor: Int) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0
        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
        paint.textSize = newSize.toFloat()
        paint.color = newColor
        paint.typeface = tf
    }
}
