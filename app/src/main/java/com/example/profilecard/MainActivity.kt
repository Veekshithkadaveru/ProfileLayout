package com.example.profilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsersApplication()
        }
    }
}

@Composable
fun UsersApplication(userProfiles: List<UserProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "user_list") {
        composable("user_list") {
            UserListScreen(userProfiles, navController)
        }
        composable(route="user_details/{userId}",
            arguments = listOf(navArgument("userId"){
                type= NavType.IntType
            })
        ) {navBackStackEntry->
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"),navController)
        }
    }
}

@Composable
fun UserListScreen(userProfiles: List<UserProfile>, navController: NavHostController?) {
    Scaffold(
        topBar = { AppBar(
            title = "Users List",
            icon = Icons.Default.Home
        ){ }
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.LightGray
            )
            {
                LazyColumn {
                    items(userProfiles) { userProfile ->
                        ProfileCard(userProfile = userProfile) {
                            navController?.navigate("user_details/${userProfile.id}")
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileDetailsScreen(userId:Int,navController: NavHostController?) {
    val userProfile= userProfileList.first { userProfile -> userId == userProfile.id }

    Scaffold(
        topBar = { AppBar(
            title = "Users List",
            icon = Icons.Default.ArrowBack)


        {
navController?.navigateUp()
        }
        }) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                ProfilePicture(userProfile.pictureUrl, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title:String,icon:ImageVector,iconClickAction:() ->Unit) {
    TopAppBar(

        navigationIcon = {
            Icon(
                icon,
                "Content Description",
                modifier = Modifier.padding(12.dp)
                    .clickable(onClick = {iconClickAction.invoke()})
            )
        },
        title = { Text(title) },
        //  modifier = Modifier.padding(horizontal = 16.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Cyan)
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .clip(MyCustomShape())
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() }),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            ProfilePicture(userProfile.pictureUrl, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }
    }
}

class MyCustomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // Define your custom path here
            moveTo(0f, -400f)
            lineTo(size.width, 40f)
            lineTo(size.width, size.height)
            lineTo(-150000f, size.height / 2f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (onlineStatus)
                Green
            else Color.Red
        ),
        modifier = Modifier
            .padding(16.dp)
            .size(imageSize),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Image(

            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureUrl)
                    .transformations(CircleCropTransformation())
                    .build()
            ),
            modifier = Modifier.size(720.dp),
            contentDescription = "ProfilePicture",
            )
    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment

    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            modifier = Modifier.alpha(0.5f),
            text = if (onlineStatus)
                "Active Now"
            else "Offline",

            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    UserProfileDetailsScreen(userId = 0,null)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UserListScreen(userProfiles = userProfileList, null)
}