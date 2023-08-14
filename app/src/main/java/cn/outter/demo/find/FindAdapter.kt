package cn.outter.demo.find

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.outter.demo.databinding.OutterItemFindUserBinding
import cn.outter.demo.find.api.FindUserApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class FindAdapter(list: MutableList<FindUserApi.FindUser>) :
    BaseQuickAdapter<FindUserApi.FindUser, FindAdapter.FindUserViewHolder>(list) {

    private var onLikeClick: OnLikeClick? = null

    fun setOnLikeClick(onLikeClick: OnLikeClick) {
        this.onLikeClick = onLikeClick
    }

    override fun onBindViewHolder(
        holder: FindUserViewHolder,
        position: Int,
        item: FindUserApi.FindUser?
    ) {
        if (item == null) {
            return
        }

        Glide.with(holder.viewBinding.userPic).load(item.photoUrl).apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(24,0))).into(holder.viewBinding.userPic)

//        holder.viewBinding.like.setOnClickListener {
//            onLikeClick?.onLikeClick(item)
//        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): FindUserViewHolder {
        return FindUserViewHolder(
            OutterItemFindUserBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    class FindUserViewHolder(val viewBinding: OutterItemFindUserBinding) :
        ViewHolder(viewBinding.root) {

    }
}