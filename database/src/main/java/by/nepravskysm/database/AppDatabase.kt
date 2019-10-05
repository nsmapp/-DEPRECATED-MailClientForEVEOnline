package by.nepravskysm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.nepravskysm.database.dao.ActiveCharacterDao
import by.nepravskysm.database.dao.AuthInfoDAO
import by.nepravskysm.database.dao.MailHeaderDao
import by.nepravskysm.database.entity.ActivaCharacterDBE
import by.nepravskysm.database.entity.AuthInfoDBE
import by.nepravskysm.database.entity.MailHeaderDBE
import by.nepravskysm.database.entity.converter.LabelConverter
import by.nepravskysm.database.entity.converter.RecipientConverter

@Database(entities = [ AuthInfoDBE::class, MailHeaderDBE::class, ActivaCharacterDBE::class], version = 8)
@TypeConverters(LabelConverter::class, RecipientConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mail_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }


    abstract fun authInfoDao() : AuthInfoDAO
    abstract fun mailHeadersDao() : MailHeaderDao
    abstract fun activeCharacterDao(): ActiveCharacterDao
}
