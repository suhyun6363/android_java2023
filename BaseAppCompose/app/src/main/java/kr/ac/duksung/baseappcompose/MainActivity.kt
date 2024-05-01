package kr.ac.duksung.baseappcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kr.ac.duksung.baseappcompose.ui.theme.BaseAppComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseAppComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column (horizontalAlignment = CenterHorizontally) {
                        var context = LocalContext.current
                        Greeting("Doohun")
                        Button(
                            onClick = {
                                Toast.makeText(context, "버튼이 눌렸어요~", Toast.LENGTH_SHORT).show()
                            }) {
                            Text("버튼")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BaseAppComposeTheme {
        Column (horizontalAlignment = CenterHorizontally) {
            var context = LocalContext.current
            Greeting("Doohun")
            Button(
                onClick = {
                    Toast.makeText(context, "버튼이 눌렸어요~", Toast.LENGTH_SHORT).show()
                }) {
                Text("버튼")
            }
        }
    }
}