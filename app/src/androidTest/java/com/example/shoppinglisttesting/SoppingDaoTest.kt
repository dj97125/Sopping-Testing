package com.example.shoppinglisttesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.shoppinglisttesting.data.local.ShoppingDao
import com.example.shoppinglisttesting.data.local.ShoppingItem
import com.example.shoppinglisttesting.data.local.ShoppingItemDataBase
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class) ///La quitamos por que especificamos nuestro propio test runner
@HiltAndroidTest
@SmallTest
class SoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDataBase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup(){
        /**
         * Estamos injectando nuestro room con hilt
         * por eso se comenta
         */
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            ShoppingItemDataBase::class.java
//        ).allowMainThreadQueries().build()
        hiltRule.inject()
        dao = database.shoppingDao()
    }
    @After
    fun teardown(){
        database.close()
    }

    /**
     * Test para launch fragments con hilt
     */

//    @Test
//    fun testLaunchFragmentInHiltContainerAddShoppingItemFragment() {
//        launchFragmentInHiltContainer<AddShoppingItemFragment> {
//        }
//    }
//    @Test
//    fun testLaunchFragmentInHiltContainerImagePick() {
//        launchFragmentInHiltContainer<ImagePickFragment> {
//        }
//    }
//    @Test
//    fun testLaunchFragmentInHiltContainerShopping() {
//        launchFragmentInHiltContainer<ShoppingFragment> {
//        }
//    }
    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1,1f,"url", 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1,1f,"url", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }
    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name1", 1,1f,"url1", id=1)
        val shoppingItem2 = ShoppingItem("name2", 2,2f,"url2", id=2)
        val shoppingItem3 = ShoppingItem("name3", 3,3f,"url3", id=3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(14)
    }
    @Test
    fun observeTotalItemSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name1", 1,1f,"url1", id=1)
        val shoppingItem2 = ShoppingItem("name2", 2,2f,"url2", id=2)
        val shoppingItem3 = ShoppingItem("name3", 3,3f,"url3", id=3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalItemSum = dao.observeTotalItem().getOrAwaitValue()

        assertThat(totalItemSum).isEqualTo(3)
    }
}
