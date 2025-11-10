package com.jordan.labweek9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jordan.labweek9.ui.theme.LabWeek9Theme
import com.jordan.labweek9.ui.theme.OnBackgroundItemText
import com.jordan.labweek9.ui.theme.OnBackgroundTitleText
import com.jordan.labweek9.ui.theme.PrimaryTextButton
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val studentListType = Types.newParameterizedType(List::class.java, Student::class.java)
private val studentListAdapter: JsonAdapter<List<Student>> = moshi.adapter(studentListType)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabWeek9Theme {
                // A surface container using the 'background' color from the
                Surface(
                    //We use Modifier.fillMaxSize() to make the surface fill the whole screen
                    modifier = Modifier.fillMaxSize(),
                    //We use MaterialTheme.colorScheme.background to get the background color
                    //and set it as the color of the surface
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Here, we call the Home composable
                    val navController = rememberNavController()
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

data class Student(
    var name:String
)


@Composable
fun App(navController: NavHostController) {
    //Here, we use NavHost to create a navigation graph
    //We pass the navController as a parameter
    //We also set the startDestination to "home"
    //This means that the app will start with the Home composable
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        //Here, we create a route called "home"
        //We pass the Home composable as a parameter
        //This means that when the app navigates to "home",
        //the Home composable will be displayed
        composable("home") {
            //Here, we pass a lambda function that navigates to "resultContent"
            //and pass the listData as a parameter
            Home { navController.navigate(
                "resultContent/?listData=$it")
            }
        }
        //Here, we create a route called "resultContent"
        //We pass the ResultContent composable as a parameter
        //This means that when the app navigates to "resultContent",
        //the ResultContent composable will be displayed
        //You can also define arguments for the route
        //Here, we define a String argument called "listData"
        //We use navArgument to define the argument
        //We use NavType.StringType to define the type of the argument
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType }
            )
        ) {
            //Here, we pass the value of the argument to the ResultContent composable
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

@Composable
fun ResultContent(listData: String) {
    val studentList = remember(listData) {
        try {
            // Use the adapter to parse the JSON. Handle nulls.
            studentListAdapter.fromJson(listData) ?: emptyList()
        } catch (e: Exception) {
            // If parsing fails (e.g., bad JSON), return an empty list
            emptyList<Student>()
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Here, we call the OnBackgroundItemText UI Element
        OnBackgroundItemText(text = listData)
    }
}




@Composable

fun Home( navigateFromHomeToResult: (String) -> Unit){
    val listData = remember { mutableStateListOf(
        Student("Tanu"),
        Student("Tina"),
        Student("Tono")
    )}

    var inputField = remember { mutableStateOf(Student("")) }
    HomeContent(
        listData,
        inputField.value,
        { input -> inputField.value = inputField.value.copy(input) },
        {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        {
            val jsonString = studentListAdapter.toJson(listData.toList())
            navigateFromHomeToResult(jsonString)
        }
    )
}


@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
        LazyColumn {
            //Here, we use item to display an item inside the LazyColumn
            item {
                Column(
                //Modifier.padding(16.dp) is used to add padding to the Column
                //You can also use Modifier.padding(horizontal = 16.dp,vertical = 8.dp)
                //to add padding horizontally and vertically
                //or Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                //to add padding to each side
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                //Alignment.CenterHorizontally is used to align the Column horizontally
                //You can also use verticalArrangement = Arrangement.Center to align the Column vertically
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OnBackgroundTitleText(text = stringResource(
                        id = R.string.enter_item)
                    )
                //Here, we use TextField to display a text input field
                TextField(
                    //Set the value of the input field
                    value = inputField.name,
                    //Set the keyboard type of the input field
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    //Set what happens when the value of the input field changes
                            onValueChange = {
                                onInputValueChange(it)
                    }
                )
                //Here, we use Button to display a button
                //the onClick parameter is used to set what happens when the button is clicked
                Row {
                        PrimaryTextButton(text = stringResource(id =
                            R.string.button_click)) {
                            onButtonClick()
                        }
                        PrimaryTextButton(text = stringResource(id =
                            R.string.button_navigate)) {
                            navigateFromHomeToResult()
                        }
                }
            }
        }
            //Here, we use items to display a list of items inside the LazyColumn
            //This is the RecyclerView replacement
            items(listData) { item ->
                Column(
                    modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OnBackgroundItemText(text = item.name)
                }
            }
        }
}

//Here, we create a preview function of the Home composable
//This function is specifically used to show a preview of the Home composable
//This is only for development purpose
//@Preview(showBackground = true)
//@Composable
//fun PreviewHome() {
//    Home()
//}




