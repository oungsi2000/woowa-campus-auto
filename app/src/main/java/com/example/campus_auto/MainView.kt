package com.example.campus_auto

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus_auto.ui.theme.CampusautoTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_auto.ui.theme.Primary
import com.example.campus_auto.ui.theme.PrimaryDanger
import com.example.campus_auto.ui.theme.Secondary
import com.example.campus_auto.ui.theme.SecondaryDanger
import kotlinx.coroutines.flow.collectLatest


@Preview(showBackground = true)
@Composable
fun MainView(viewModel: MainViewModel = viewModel()) {
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
                viewModel = viewModel,
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
                Text(stringResource(R.string.topbar_text))
            }
        }
    )
}

@Composable
fun AutoCheckInStart(viewModel: MainViewModel, modifier: Modifier) {
    var isEnabled by remember { mutableStateOf(false) }
    val hasAllPermission by viewModel.hasAllPermission.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-100).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.description_auto_check),
        )

        if (!hasAllPermission) {
            AutoCheckButtonDisabled()
            GoToSettingButton(viewModel)
            return
        }

        if (!isEnabled) {
            AutoCheckButtonEnable {
                isEnabled = true
            }
        } else {
            AutoCheckButtonStop {
                isEnabled = false
            }
        }
    }
}

@Composable
fun AutoCheckButtonEnable(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(260.dp)
            .height(48.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.PrimaryDanger,
            contentColor = Color.White
        )
    ) {
        Text(
            fontSize = 14.sp,
            text = stringResource(R.string.auto_check_start)
        )
    }
}

@Composable
fun AutoCheckButtonStop(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(260.dp)
            .height(48.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Primary,
            contentColor = Color.White
        )
    ) {
        Text(
            fontSize = 14.sp,
            text = stringResource(R.string.auto_check_stop)
        )
    }
}

@Composable
fun AutoCheckButtonDisabled() {
    Button(
        onClick = {},
        modifier = Modifier
            .width(260.dp)
            .height(48.dp),
        enabled = false,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.Gray,
        )
    ) {
        Text(
            fontSize = 14.sp,
            text = stringResource(R.string.auto_check_disabled)
        )
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
                .offset(x = 100.dp, y = 300.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BottomNetWorkBanner() {
    var isEnabled by remember { mutableStateOf(false) }

    if (isEnabled) {
        BottomNetWorkEnabled()
    } else {
        BottomNetWorkDisabled()
    }
}

@Composable
fun BottomNetWorkEnabled() {
    BottomAppBar(
        containerColor = Color.Secondary,
        modifier = Modifier.height(72.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.tool_current_ip),
                color = Color.Black,
            )
            Text(
                text = stringResource(R.string.can_check_in),
                color = Color.Black,
            )
        }
    }
}

@Composable
fun BottomNetWorkDisabled() {
    BottomAppBar(
        containerColor = Color.SecondaryDanger,
        modifier = Modifier.height(72.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.tool_current_ip),
                color = Color.Black,
            )
            Text(
                text = stringResource(R.string.cannot_check_in),
                color = Color.Black,
            )
        }
    }
}

@Composable
fun GoToSettingButton(viewModel: MainViewModel) {
    Text(
        text = "권한을 설정하러 가볼까요?",
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .padding(12.dp)
            .clickable {
                viewModel.setPermission()
            }
        ,
        color = Color.Gray
    )
}
