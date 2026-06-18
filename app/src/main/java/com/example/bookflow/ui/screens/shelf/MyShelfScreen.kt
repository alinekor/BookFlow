package com.example.bookflow.ui.screens.shelf

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookflow.presentation.shelf.MyShelfScreenState
import com.example.bookflow.presentation.shelf.MyShelfViewModel

@Composable
fun MyShelfScreen(
    router: IMyShelfRouter,
    viewModel: MyShelfViewModel = viewModel(
        factory = MyShelfViewModel.Factory,
    )
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()

    MyShelfScreen(
        screenState = screenState,
        router = router,
    )
}

@Composable
fun MyShelfScreen(
    screenState: MyShelfScreenState,
    router: IMyShelfRouter,
) {

}

//@Preview(name = "Light Theme", showBackground = true)
//@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun MyShelfScreenPreview() {
//    BookFlowTheme {
//        MyShelfScreen()
//    }
//}