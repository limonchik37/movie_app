package cz.cvut.bekalim.app.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.bekalim.app.data.NetworkModule
import cz.cvut.bekalim.app.data.movie.MovieApi
import cz.cvut.bekalim.app.data.movie.MovieDetailScreen
import cz.cvut.bekalim.app.data.movie.MovieDetailViewModel
import cz.cvut.bekalim.app.data.movie.MovieRepository
import cz.cvut.bekalim.app.data.movie.MoviesScreen
import cz.cvut.bekalim.app.data.movie.MoviesViewModel
import cz.cvut.bekalim.app.data.review.AddReviewScreen
import cz.cvut.bekalim.app.data.review.AddReviewViewModel
import cz.cvut.bekalim.app.data.review.ReviewApi
import cz.cvut.bekalim.app.data.review.ReviewRepository
import cz.cvut.bekalim.app.data.security.AuthApi
import cz.cvut.bekalim.app.data.security.AuthRepository
import cz.cvut.bekalim.app.data.security.AuthViewModel
import cz.cvut.bekalim.app.ui.LoginScreen
import cz.cvut.bekalim.app.ui.RegisterScreen


sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Register : Screen("register")
    object Movies   : Screen("movies")
    object Detail   : Screen("detail/{id}") {
        fun createRoute(id: Long) = "detail/$id"
    }
    object AddReview  : Screen("addReview/{movieId}") {
        fun createRoute(movieId: Long) = "addReview/$movieId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    val authVM: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(
            AuthRepository(
                NetworkModule.retrofit.create(AuthApi::class.java)
            )
        )
    )

    NavHost(
        navController   = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authVM,
                onLoginSuccess = {
                    // ← only navigate when the user actually taps “Log in”
                    navController.navigate(Screen.Movies.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authVM,
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Movies.route) {
            val moviesVM: MoviesViewModel = viewModel(
                factory = MoviesViewModel.Factory(
                    MovieRepository(
                        NetworkModule.retrofit.create(MovieApi::class.java)
                    )
                )
            )
            MoviesScreen(
                moviesVM = moviesVM,
                onSelect  = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments!!.getLong("id")
            val detailVM: MovieDetailViewModel = viewModel(
                factory = MovieDetailViewModel.Factory(
                    MovieRepository(NetworkModule.retrofit.create(MovieApi::class.java)),
                    ReviewRepository(NetworkModule.retrofit.create(ReviewApi::class.java)),
                    movieId = movieId
                )
            )
            MovieDetailScreen(
                viewModel    = detailVM,
                onBack       = { navController.popBackStack() },
                onAddReview  = { navController.navigate(Screen.AddReview.createRoute(movieId)) },
//                onAddPlanned = { /* TODO: логика отметки как ‘plannned’ */ }
            )
        }
    }
}