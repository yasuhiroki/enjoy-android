package jp.yasuhiroki.enjoyandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import jp.yasuhiroki.enjoyandroid.ui.theme.EnjoyAndroidTheme

class MainActivity : ComponentActivity() {

    val src = MutableLiveData<String>("initial value is not pass to dest in lifecycle version 2.7.0")
    val dest = src.map { it }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dest.observe(this) {
            Log.d("MainActivity", "dest: $it")
        }
        src.postValue("hello")

        setContent {
            EnjoyAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EnjoyAndroidTheme {
        Greeting("Android")
    }
}