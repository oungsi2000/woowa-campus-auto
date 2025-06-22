package com.example.campus_auto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus_auto.ui.theme.CampusautoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainView()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainView() {
    CampusautoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar()
            },
            bottomBar = {
                BottomNetWorkBanner()
            }
        ) { innerPadding ->
            MainBackground()
            AutoCheckInStart(
                name = "Android",
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("우아한테크코스")
            }
        }
    )
}

@Composable
fun AutoCheckInStart(name: String, modifier: Modifier = Modifier) {
    var isEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-100).dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(16.dp),
            text = "자동으로 등/하교를 기록합니다",
        )

        if (!isEnabled) {
            Button(
                onClick = { isEnabled = true },
                modifier = Modifier
                    .width(260.dp)
                    .height(48.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00AFF0),
                    contentColor = Color.White
                )
            ) {
                Text(
                    fontSize = 14.sp,
                    text = "자동등교 시작"
                )
            }
        } else {
            Button(
                onClick = { isEnabled = false },
                modifier = Modifier
                    .width(260.dp)
                    .height(48.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDF3E62),
                    contentColor = Color.White
                )
            ) {
                Text(
                    fontSize = 14.sp,
                    text = "자동등교 해제"
                )
            }
        }
    }
}

@Composable
fun MainBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.haengseoungee),
            contentDescription = "background",
            modifier = Modifier
                .scale(2f)
                .offset(x = 100.dp, y = 300.dp)
            ,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BottomNetWorkBanner() {
    var isEnabled by remember { mutableStateOf(false) }

    if (isEnabled) {
        BottomAppBar(
            containerColor = Color(0xFFA6DAF4),
            modifier = Modifier.height(72.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "현재 IP : 192.168.0.1",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "현재 등/하교가 가능합니다",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    } else {
        BottomAppBar(
            containerColor = Color(0xFFF27B92),
            modifier = Modifier.height(72.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "현재 IP : 192.168.0.1",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "현재 등/하교가 불가능합니다",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


