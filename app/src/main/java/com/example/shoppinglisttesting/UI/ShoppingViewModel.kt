package com.example.shoppinglisttesting.UI

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglisttesting.Repositories.ShoppingRepository
import com.example.shoppinglisttesting.data.local.ShoppingItem
import com.example.shoppinglisttesting.data.remote.responses.ImageResponse
import com.example.shoppinglisttesting.oher.Constants
import com.example.shoppinglisttesting.oher.Event
import com.example.shoppinglisttesting.oher.Resource
import kotlinx.coroutines.launch
import java.lang.Exception


class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<Event<Resource<String>>>()
    val curImageUrl: LiveData<Event<Resource<String>>> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        if(url.isEmpty()){
            _curImageUrl.postValue(Event(Resource.error(
                "The field must not be empty", null)))
        }

       // _curImageUrl.postValue(url)
    }


    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {

        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The fields must not be empty", null)))
        }
        if (name.length > Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The name of the item" + "must not exceed ${Constants.MAX_NAME_LENGTH}" +
                        " characters", null)))
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The price of the item" + "must not exceed ${Constants.MAX_PRICE_LENGTH}" +
                        " characters", null)))
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception){
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "Please enter a valid amount", null)))
            return
        }

        val shoppingItem = ShoppingItem(name,amount,priceString.toFloat(),
            (_curImageUrl.value ?: "") as String
        )
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")///Tested
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()){
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}
