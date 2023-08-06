package cn.outter.demo.find

import cn.outter.demo.find.api.FindUserApi

interface OnLikeClick {
    fun onLikeClick(findUser: FindUserApi.FindUser?)
}