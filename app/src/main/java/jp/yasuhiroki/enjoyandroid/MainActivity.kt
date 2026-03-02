package jp.yasuhiroki.enjoyandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.yasuhiroki.enjoyandroid.ui.theme.EnjoyAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnjoyAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content() {
    Column {
        Text("TextField", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))

        Text("value-based")
        var text by remember { mutableStateOf("Hello") }
        ValueBasedTextField(value = text, onValueChange = { text = it })

        Spacer(Modifier.height(8.dp))

        Text("state-based")
        val state = rememberTextFieldState("Hello")
        StateBasedTextField(state)
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    EnjoyAndroidTheme {
        Content()
    }
}

@Composable
fun ValueBasedTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(value = value, onValueChange = onValueChange)
}

@Composable
fun StateBasedTextField(
    state: TextFieldState
) {
    TextField(state = state)
}