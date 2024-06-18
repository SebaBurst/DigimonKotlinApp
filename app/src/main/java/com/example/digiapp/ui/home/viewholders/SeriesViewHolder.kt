import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.databinding.ItemSeriesBinding
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.example.digiapp.R
import com.example.digiapp.data.models.SeriesItemResultItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class SeriesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemSeriesBinding.bind(view)

    fun bind(serieItem: SeriesItemResultItem) {
        // Load series icon
        Picasso.get().load(serieItem.icon).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                binding.seriesIcon.setImageBitmap(bitmap)
                // Set gradient background after image is loaded
                val colors = getDominantGradient(bitmap)
                val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)
                gradientDrawable.cornerRadius = 20f  // Set corner radius for gradient drawable
                binding.seriesBackground.setBackgroundDrawable(gradientDrawable)
                binding.seriesBackground.strokeWidth = 0
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                // Handle the error
                errorDrawable?.let {
                    binding.seriesIcon.setImageDrawable(it)
                }
                // Add error message here
                Toast.makeText(binding.root.context, "Error loading series icon", Toast.LENGTH_SHORT).show()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                // Handle the placeholder
                placeHolderDrawable?.let {
                    binding.seriesIcon.setImageDrawable(it)
                }
            }
        })

        // Load series logo
        Picasso.get().load(serieItem.logo).into(binding.seriesLogo)
    }

    private fun getDominantGradient(bitmap: Bitmap): IntArray {
        val palette = Palette.from(bitmap).generate()
        val dominantColor = palette.getDominantColor(ContextCompat.getColor(binding.root.context, R.color.white))
        val lightMutedColor = palette.getLightMutedColor(ContextCompat.getColor(binding.root.context, R.color.white))
        return intArrayOf(dominantColor, lightMutedColor)
    }

   
}
