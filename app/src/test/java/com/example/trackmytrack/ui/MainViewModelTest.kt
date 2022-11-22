package com.example.trackmytrack.ui

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.trackmytrack.MainCoroutineRule
import com.example.trackmytrack.data.FakeDataSource
import com.example.trackmytrack.data.Record
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class MainViewModelTest{    /** Not completed **/

    // Subjects under test
    private lateinit var testViewModel: MainViewModel
    private lateinit var fakeDataSource: FakeDataSource

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    // Executes each task synchronously using Architecture Components.

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )

    @get:Rule
    val dataExample = Record("12/1", "", "6:5:4", 37.795490, -122.394276)

    @Before
    fun setupViewModel() = runBlockingTest {
        fakeDataSource = FakeDataSource()
        val data1 = Record("10/1", "", "2:10:12", 29.975507526586643, 31.40644697381402)
        val data2 = Record("11/1", "", "12:30:1", 37.819927, -122.478256)
        fakeDataSource.saveRecord(data1)
        fakeDataSource.saveRecord(data2)

//        val repo = (ApplicationProvider.getApplicationContext() as MyApp).repository
        testViewModel = MainViewModel(fakeDataSource)
    }

    /**
     @Test
     fun subjectUnderTest_actionOrInputCase_resultState () {
     // 1- Given (parameter(s) or data to be used)
     ... .. .. ...
     ... .. .. ...

     // 2- Call the function with the given data (inside a result variable)
     val result = actualFunction(parameter)

     // 3- Check the result using assertions [assertEquals () / assertThat () / .. ]
     assertEquals(expected , actual/result)
     **/

    @Test
    fun saveRecord_checkRepository() {
        mainCoroutineRule.pauseDispatcher()

        testViewModel.saveRecord(dataExample)

        mainCoroutineRule.resumeDispatcher()

        assertEquals(testViewModel.allTrackRecordsList.data?.value?.get(0) ?: false, dataExample)
    }

    @Test
    fun clearRecords_checkIfEmpty() {
        mainCoroutineRule.pauseDispatcher()

        testViewModel.saveRecord(dataExample)
        testViewModel.clearRecords()

        mainCoroutineRule.resumeDispatcher()

    }

}