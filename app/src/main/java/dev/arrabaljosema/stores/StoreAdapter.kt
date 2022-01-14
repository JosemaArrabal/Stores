package dev.arrabaljosema.stores

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.arrabaljosema.stores.databinding.ItemStoreBinding

class StoreAdapter {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoreBinding.bind(view)
    }
}