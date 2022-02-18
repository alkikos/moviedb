/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
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

package com.example.movieapp

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.movieapp.api.Api
import com.example.movieapp.model.Movie
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.skydoves.themovies2.extension.circularRevealedAtCenter
import com.skydoves.themovies2.extension.requestGlideListener

object ViewBinding {

  @JvmStatic
  @BindingAdapter("loadCircleImage")
  fun bindLoadCircleImage(view: AppCompatImageView, url: String) {
    Glide.with(view.context)
      .load(url)
      .apply(RequestOptions().circleCrop())
      .into(view)
  }

  @JvmStatic
  @BindingAdapter("loadPaletteImage", "loadPaletteTarget")
  fun bindLoadImage(view: AppCompatImageView, url: String, targetView: View) {
    Glide.with(view)
      .load(url)
      .listener(

        GlidePalette.with(url)
          .use(BitmapPalette.Profile.VIBRANT)
          .intoBackground(targetView)
          .crossfade(true)
      )
      .into(view)
  }




  @JvmStatic
  @BindingAdapter("bindBackDrop")
  fun bindBackDrop(view: ImageView, movie: Movie?) {
    Glide.with(view.context).load(Api.getBackdropPath(movie?.imageEndPoint))
      .listener(view.requestGlideListener())
      .into(view)
  }

  @JvmStatic
  @SuppressLint("SetTextI18n")
  @BindingAdapter("bindReleaseDate")
  fun bindReleaseDate(view: TextView, movie: Movie?) {
    view.text = "Release Date : ${movie?.releaseDate}"
  }




}
