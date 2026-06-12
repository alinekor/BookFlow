package com.example.bookflow.ui.extensions

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlin.reflect.KClass

fun <T : Any> NavDestination?.isInHierarchy(route: KClass<T>): Boolean {
    return this?.hierarchy?.any { it.hasRoute(route) } == true
}