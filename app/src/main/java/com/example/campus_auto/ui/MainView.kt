package com.example.campus_auto.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus_auto.ui.theme.CampusautoTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_auto.MainViewModel
import com.example.campus_auto.R
import com.example.campus_auto.ui.theme.Primary
import com.example.campus_auto.ui.theme.PrimaryDanger
import com.example.campus_auto.ui.theme.Secondary
import com.example.campus_auto.ui.theme.SecondaryDanger
import com.example.campus_auto.uimodel.LoadingState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun MainView(
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.loadingState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    if (state == LoadingState.Loading) {
        TopBar()
        Loading()
        return
    }

    PermissionLauncher(snackBarHostState)
    CampusautoTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar()
            },
            bottomBar = {
                BottomNetWorkBanner(viewModel)
            }
        ) { innerPadding ->
            AutoCheckInStart(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(innerPadding)
            )
            MainBackground(viewModel)
        }
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun PermissionLauncher(snackBarHostState: SnackbarHostState) {
    val coroutineScope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = "권한을 설정하지 않으면 서비스 이용이 불가능합니다",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBackground(viewModel: MainViewModel) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.setConnectionInfo()
    })

    Box(
        Modifier
            .pullRefresh(pullRefreshState)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Image(
                painter = painterResource(id = R.drawable.haengseoungee),
                contentDescription = "background",
                modifier = Modifier
                    .scale(2f)
                    .offset(x = 100.dp, y = 300.dp),
                contentScale = ContentScale.Crop
            )
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun BottomNetWorkBanner(viewModel: MainViewModel) {
    val connectionInfo by viewModel.connectionInfo.collectAsState()
    val connectionName = connectionInfo.ipAddress?.let {
        stringResource(
            R.string.tool_current_ip,
            it
        )
    } ?: stringResource(R.string.no_network_connected)

    if (connectionInfo.isEnabled) {
        BottomNetWorkEnabled(connectionName)
    } else {
        BottomNetWorkDisabled(connectionName)
    }
}

@Composable
fun BottomNetWorkEnabled(connectionName: String) {
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
                text = connectionName,
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
fun BottomNetWorkDisabled(connectionName: String) {
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
                text = connectionName,
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
                viewModel.setAccessibilityPermission()
            },
        color = Color.Gray
    )
}
