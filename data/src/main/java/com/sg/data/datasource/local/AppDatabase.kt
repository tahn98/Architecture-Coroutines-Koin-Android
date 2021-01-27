package com.sg.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sg.data.datasource.local.AppDatabase.Companion.DB_VERSION
import com.sg.domain.entity.Message
import com.sg.domain.TokenConverter
import com.sg.domain.User
import com.sg.domain.dao.MessageDao
import com.sg.domain.dao.UserDao

@Database(entities = [User::class, Message::class], version = DB_VERSION, exportSchema = false)
@TypeConverters(TokenConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "Covid.DB"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: build(context).also { INSTANCE = it }
        }

        private fun build(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_TO_2)
                .build()

        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }

    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
}