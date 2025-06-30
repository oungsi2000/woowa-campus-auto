package com.example.campus_auto.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.campus_auto.view.main.MainViewModel
import com.example.campus_auto.R
import com.example.campus_auto.ui.theme.Primary
import com.example.campus_auto.ui.theme.PrimaryDanger
import com.example.campus_auto.ui.theme.Secondary
import com.example.campus_auto.ui.theme.SecondaryDanger
import com.example.campus_auto.uimodel.LoadingState
import kotlinx.coroutines.CoroutineScope
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
            MainBackground(viewModel)
            AutoCheckInStart(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(innerPadding),
                snackBarHostState = snackBarHostState
            )
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
            coroutineScope.showSnackBar(
                text = "권한을 설정하지 않으면 서비스 이용이 불가능합니다",
                snackBarHostState
            )
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
fun AutoCheckInStart(viewModel: MainViewModel, modifier: Modifier, snackBarHostState: SnackbarHostState) {
    val isEnabled by viewModel.isServiceEnabled.collectAsState()
    val hasAllPermission by viewModel.hasAllPermission.collectAsState()
    val isAppInstalled by viewModel.isAppInstalled.collectAsState()
    val coroutineScope = rememberCoroutineScope()

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

        if (!isAppInstalled) {
            AutoCheckButtonDisabled()
            NotifyAppNotInstalled(viewModel)
            return
        }

        if (!hasAllPermission) {
            AutoCheckButtonDisabled()
            GoToSettingButton(viewModel)
            return
        }

        if (!isEnabled) {
            AutoCheckButtonEnable {
                viewModel.toggleService()
                coroutineScope.showSnackBar(
                    text = "자동등교가 활성화되었습니다",
                    snackBarHostState
                )
            }
        } else {
            AutoCheckButtonStop {
                viewModel.toggleService()
                coroutineScope.showSnackBar(
                    text = "자동등교가 비활성화되었습니다",
                    snackBarHostState
                )
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
        Column(
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
                viewModel.publishPermissionEvent()
            },
        color = Color.Gray
    )
}

@Composable
fun NotifyAppNotInstalled(viewModel: MainViewModel) {
    Text(
        text = "캠퍼스 앱을 찾을 수 없어요...",
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .padding(12.dp),
        color = Color.Gray
    )
}

fun CoroutineScope.showSnackBar(
    text:String,
    snackBarHostState: SnackbarHostState
) {
    launch {
        snackBarHostState.currentSnackbarData?.dismiss()
        snackBarHostState.showSnackbar(
            message = text,
            duration = SnackbarDuration.Short
        )
    }
}
