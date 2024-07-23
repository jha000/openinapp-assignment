package com.console.openinapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.console.openinapp.model.Link
import com.console.openinapp.databinding.ItemRecentLinkBinding
import com.squareup.picasso.Picasso

class LinkAdapter : ListAdapter<Link, LinkAdapter.RecentLinkViewHolder>(RecentLinkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentLinkViewHolder {
        val binding = ItemRecentLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentLinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentLinkViewHolder, position: Int) {
        val recentLink = getItem(position)
        holder.bind(recentLink)
    }

    class RecentLinkViewHolder(private val binding: ItemRecentLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recentLink: Link) {
            binding.recentLink = recentLink
            binding.executePendingBindings()

            Picasso.get()
                .load(recentLink.original_image)
                .into(binding.recentLinkImage)

            binding.openWebLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recentLink.web_link))
                itemView.context.startActivity(intent)
            }

            binding.copy.setOnClickListener {
                val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Link", recentLink.web_link)
                clipboard.setPrimaryClip(clip)
            }

        }
    }

    class RecentLinkDiffCallback : DiffUtil.ItemCallback<Link>() {
        override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean {
            return oldItem.url_id == newItem.url_id
        }

        override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
            return oldItem == newItem
        }
    }
}
