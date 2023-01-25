package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.Asteroid
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.main.ApiStatus
import com.udacity.asteroidradar.recycle_adapter.AsteroidsListAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
@BindingAdapter("listAsteroid")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidsListAdapter
    adapter.submitList(data)
}
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, img: PictureOfDay?) {
    if (img != null) {
        if(img.url.isEmpty()) {
            val imgUri = img.url.toUri().buildUpon().scheme("https").build()
            Picasso.with(imgView.context)
                .load(imgUri)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .resize(50, 50)
                .centerCrop()
                .into(imgView)
        }else{
            Picasso.with(imgView.context)
                .load(R.drawable.placeholder_picture_of_day)
                .resize(50, 50)
                .centerCrop()
                .into(imgView)
        }
    }
}
@BindingAdapter("apiStatus")
fun bindStatus(loadingStatus: ProgressBar, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            loadingStatus.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
            loadingStatus.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            loadingStatus.visibility = View.GONE
        }
    }
}