package jp.yasuhiroki.enjoyandroid

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jp.yasuhiroki.enjoyandroid.ui.theme.EnjoyAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnjoyAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListWorkHistory(context = this@MainActivity)
                }
            }
        }
    }
}

@Composable
fun ListWorkHistory(context: Context) {
    val sharedPref = context.getSharedPreferences("yasuhiroki", Context.MODE_PRIVATE)
    val workHistorySet = sharedPref.getStringSet("work_history", emptySet()) ?: emptySet()
    val workHistoryList = workHistorySet.toList().sorted()

    LazyColumn {
        items(workHistoryList) { item ->
            Text(text = item)
        }
    }
}