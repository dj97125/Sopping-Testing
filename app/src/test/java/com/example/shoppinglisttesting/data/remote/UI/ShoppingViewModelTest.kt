package com.example.shoppinglisttesting.data.remote.UI

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shoppinglisttesting.UI.ShoppingViewModel
import com.example.shoppinglisttesting.data.remote.repositories.FakeShoppingRepository
import com.example.shoppinglisttesting.data.remote.repositories.getOrAwaitValueTest
import com.example.shoppinglisttesting.oher.Constants
import com.example.shoppinglisttesting.oher.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCorroutineRule = MainCorroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }
    @Test
    fun `insert CurlImageUrl empty field, returns error`(){
        viewModel.setCurImageUrl("")

        val value = viewModel.curImageUrl.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert shopping item with empty field, returns error`(){
        viewModel.insertShoppingItem("name","", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert shopping item with too long name, returns error`(){
        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1){
                append(1)
            }
        }
        viewModel.insertShoppingItem(string,"5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert shopping item with too long price, returns error`(){
        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name","5", string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert shopping item with too high amount, returns error`(){
        viewModel.insertShoppingItem("name","99999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert shopping item with valid input, returns success`(){
        viewModel.insertShoppingItem("name","9", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)

    }
}