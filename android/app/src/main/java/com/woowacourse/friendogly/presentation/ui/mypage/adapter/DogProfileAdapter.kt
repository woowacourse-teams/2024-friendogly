package com.woowacourse.friendogly.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemDogBinding
import com.woowacourse.friendogly.presentation.ui.mypage.Dog
import com.woowacourse.friendogly.presentation.ui.mypage.MyPageActionHandler

class DogProfileAdapter(
    private val actionHandler: MyPageActionHandler,
) : ListAdapter<Dog, DogProfileAdapter.ViewHolder>(DogItemDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDogBinding.inflate(inflater, parent, false)
        binding.actionHandler = actionHandler
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Dog) {
            binding.dog = item
        }
    }

    internal object DogItemDiffCallback : DiffUtil.ItemCallback<Dog>() {
        override fun areItemsTheSame(
            oldItem: Dog,
            newItem: Dog,
        ): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(
            oldItem: Dog,
            newItem: Dog,
        ): Boolean = oldItem == newItem
    }
}
