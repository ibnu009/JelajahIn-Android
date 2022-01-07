package com.ibnu.jelajahin.core.data.local.room.query

import androidx.sqlite.db.SimpleSQLiteQuery

object TypeUtils {

    const val FAVORITE_WISATA = "wisata favorite"
    const val FAVORITE_PENGINAPAN = "penginapan favorite"
    const val FAVORITE_RESTAURANT = "restaurant favorite"

    fun getFavoriteByType(type: String, savedBy: String): SimpleSQLiteQuery {
        val simpleSQLiteQuery = StringBuilder().append("SELECT * FROM favorite ")
        when (type) {
            FAVORITE_WISATA -> {
                simpleSQLiteQuery.append("WHERE favorite_type ='$FAVORITE_WISATA' ")
            }
            FAVORITE_PENGINAPAN -> {
                simpleSQLiteQuery.append("WHERE favorite_type ='$FAVORITE_PENGINAPAN' ")
            }
            FAVORITE_RESTAURANT -> {
                simpleSQLiteQuery.append("WHERE favorite_type ='$FAVORITE_RESTAURANT' ")
            }
        }
        simpleSQLiteQuery.append("AND saved_by = '$savedBy'")
        return SimpleSQLiteQuery(simpleSQLiteQuery.toString())
    }
}