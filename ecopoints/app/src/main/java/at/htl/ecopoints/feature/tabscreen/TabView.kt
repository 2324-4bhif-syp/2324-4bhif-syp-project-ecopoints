package at.htl.leonding.feature.tabscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.htl.leonding.feature.home.HomeView
import at.htl.leonding.feature.settings.SettingsScreen
import at.htl.leonding.feature.todo.ToDoView
import at.htl.leonding.model.Store
import at.htl.leonding.ui.theme.ToDoTheme
import javax.inject.Inject

class TabView @Inject constructor() {
    @Inject
    lateinit var tabScreenViewModel: TabViewModel
    @Inject
    lateinit var homeScreenView: HomeView
    @Inject
    lateinit var toDoView: ToDoView

    @Composable
    fun TabViewLayout() {
        val model = tabScreenViewModel.subject.subscribeAsState(tabScreenViewModel.value)
        val tab = model.value.selectedTab
        val tabIndex = tab.index()
        val selectedTab = remember { mutableIntStateOf(tabIndex) }
        val numberOfTodos = model.value.numberOfToDos
        val tabs = listOf("Home", "Trip", "Ranking", "Profile")
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = selectedTab.intValue) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = selectedTab.intValue == index,
                        onClick = {
                            selectedTab.intValue = index
                            tabScreenViewModel.selectTabByIndex(index)
                        },
                        icon = {
                            when (index) {
                                0 -> Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                                1 -> Icon(imageVector = Icons.Default.Place, contentDescription = "Home")
                                2 -> Icon(imageVector = Icons.Default.List, contentDescription = "Home")
                                3 -> Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                            }
                        }
                    )
                }
            }
            ContentArea(selectedTab.intValue)
        }
    }
    @Composable
    fun ContentArea(selectedTab: Int) {
        if (LocalInspectionMode.current) {
            PreviewContentArea()
        } else {
            when (selectedTab) {
                0 -> homeScreenView.HomeScreen()
                1 -> toDoView.ToDos()
                2 -> SettingsScreen()
            }
        }
    }
    @Composable
    fun PreviewContentArea() {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)) {
                Text(text = "Content area of the selected tab", softWrap = true)
            }
        }
    }
    @Preview(showBackground = true)
    @Preview(name="150%", fontScale = 1.5f)
    @Composable
    fun TabViewPreview() {
        if (LocalInspectionMode.current) {
            tabScreenViewModel = TabViewModel(Store())
            ToDoTheme {
                TabViewLayout()
            }
        }
    }
}




