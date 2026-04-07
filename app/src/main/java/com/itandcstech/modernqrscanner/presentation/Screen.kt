package com.itandcstech.modernqrscanner.presentation

/**
 * @Created by Ashif on 07-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
// Sealed class = limited set of types
// Koi bhi random string route nahi bana sakta
// Sirf yahi 3 routes valid hain
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Camera : Screen("camera")
}