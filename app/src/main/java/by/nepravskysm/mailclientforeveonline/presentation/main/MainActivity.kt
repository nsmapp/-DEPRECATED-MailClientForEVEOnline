package by.nepravskysm.mailclientforeveonline.presentation.main


import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import by.nepravskysm.domain.entity.UnreadMailsCount
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.CharacterChangeDialog
import by.nepravskysm.mailclientforeveonline.utils.*
import by.nepravskysm.mailclientforeveonline.workers.CheckNewMailWorker
import by.nepravskysm.rest.api.createAuthUrl
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_navigation_menu.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), CharacterChangeDialog.ChangeCharacterListener {


    private val vModel: MainViewModel by viewModel()
    private lateinit var navHeader: View
    private lateinit var navigationController: NavController
    private lateinit var pref: SharedPreferences
    private var loginListener: LoginListener? = null
    private val characterListDialog = CharacterChangeDialog()

    private val progresBarObserver = Observer<Boolean>{
        if(it){showProgresBar()
        }else{hideProgresBar()
            loginListener?.refreshDataAfterLogin()
        }
    }
    private val activeCharacterObserver = Observer<String> { name ->
        characterName.text = name
        if (!name.equals("")) {
            pref.edit().putBoolean(CHARACTER_IS_AUTH, true).apply()
        }
    }
    private val characterIdObserver = Observer<Long>{id ->
            pastImage(activeCharacter, id)}
    private val errorObserver = Observer<Long> { eventId ->
        makeToastMessage(this, eventId)
    }
    private val unreadMailObserver = Observer<UnreadMailsCount>{mail ->
        showUnreadMailInMenu(R.id.inboxFragment, mail.inbox)
        showUnreadMailInMenu(R.id.sendFragment, mail.send)
        showUnreadMailInMenu(R.id.corpFragment, mail.corporation)
        showUnreadMailInMenu(R.id.allianceFragment, mail.alliance)
        showUnreadMailInMenu(R.id.mailingListFragment, mail.mailingList)

        if (mail.getAllUnreadMailsCount() != 0) {
            showSnackeMessage("You have ${mail.getAllUnreadMailsCount()} unread mails")
        }
    }


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        pref = getSharedPreferences(SETTINGS, MODE_PRIVATE)

        if(pref.getBoolean(DARK_MODE, false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        navHeader = navigationView.getHeaderView(0)
        navigationController = findNavController(R.id.hostFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newMailFragment,
                R.id.inboxFragment,
                R.id.sendFragment,
                R.id.corpFragment,
                R.id.allianceFragment,
                R.id.mailingListFragment,
                R.id.aboutFragment,
                R.id.settingsFragment),
            drawerLayout)

        navigationView.setupWithNavController(navigationController)

        initOnClickListner()
        checkAuthCode()

        characterListDialog.setChangeCharacterListener(this)

        vModel.getActiveCharacterInfo()

        vModel.characterName.observe(this, activeCharacterObserver)
        vModel.characterId.observe(this, characterIdObserver)
        vModel.isVisibilityProgressBar.observe(this, progresBarObserver)
        vModel.eventId.observe(this, errorObserver)
        vModel.unreadMailsCount.observe(this, unreadMailObserver)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val syncPeriodically = PeriodicWorkRequestBuilder<CheckNewMailWorker>(15, TimeUnit.MINUTES,
            10, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "newMailWorker",
                ExistingPeriodicWorkPolicy.REPLACE,
                syncPeriodically)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.hostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun characterChanged() {
        if(loginListener != null){
            loginListener?.refreshDataAfterLogin()
        }
        vModel.getActiveCharacterInfo()

    }

    private fun startBrowser(){
        val url: String = createAuthUrl()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun checkAuthCode(){
        if(intent.data != null){
            if(intent.data?.getQueryParameter("code") != null){
                val code: String = intent.data!!.getQueryParameter("code")!!
                vModel.startAuth(code)
            }else{
                if(loginListener != null){
                    loginListener?.refreshDataAfterLogin()
                }
            }
        }

    }

    fun setUnreadMailsCount(){
        vModel.getUnreadMails()
    }

    private fun initOnClickListner(){

        navMenu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        activeCharacter.setOnClickListener {
            characterListDialog.show(supportFragmentManager, "characterList")
        }

    }

    private fun showUnreadMailInMenu(itemMenuId: Int, count: Int) {
        if(count != 0){
            navigationView.menu.findItem(itemMenuId).actionView.unreadMails.text = "$count"
        } else {
            navigationView.menu.findItem(itemMenuId).actionView.unreadMails.text = ""
        }
    }

    private fun showProgresBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgresBar(){
        progressBar.visibility = View.GONE
    }

    fun setLoginListnerInterface(loginListener: LoginListener) {
        this.loginListener = loginListener

    }

    fun showSnackeMessage(message: String) {
        val snacke = Snackbar.make(drawerLayout, message, Snackbar.LENGTH_LONG)

        snacke.show()
    }

    interface LoginListener{
        fun refreshDataAfterLogin()
    }



}


