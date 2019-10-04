package by.nepravskysm.mailclientforeveonline.presentation.main


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.rest.api.createAuthUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import org.koin.android.viewmodel.ext.android.viewModel



class MainActivity : AppCompatActivity() {

    private val vModel: MainViewModel by viewModel()

    private lateinit var navHeader: View
    private lateinit var navigationController: NavController

    private val progresBarObserver = Observer<Boolean>{
        if(it){showProgresBar()
        }else{hideProgresBar()
            navigationController.navigate(R.id.inboxFragment)}
    }
    val nameObserver = Observer<String>{name -> navHeader.rootView.charName.text = name}
    val characterIdObserver = Observer<Long>{id ->
        Picasso.get()
            .load("https://imageserver.eveonline.com/Character/${id}_128.jpg")
            .into(navHeader.rootView.characterPhoto)
        }


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHeader = navigationView.getHeaderView(0)
        navigationController = findNavController(R.id.hostFragment)

        setSupportActionBar(toolBar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newMailFragment,
                R.id.inboxFragment,
                R.id.sendFragment,
                R.id.corpFragment,
                R.id.allianceFragment,
                R.id.mailingListFragment,
                R.id.saveMailsListFragment,
                R.id.settingsActivity,
                R.id.aboutFragment),
            drawerLayout)

        setupActionBarWithNavController(navigationController, appBarConfiguration)
        navigationView.setupWithNavController(navigationController)

        checkAuthCode()



        vModel.characterName.observe(this, nameObserver)
        vModel.characterId.observe(this, characterIdObserver)

        navHeader.rootView.addCharacter.setOnClickListener {
            startBrowser()

        }

        vModel.isVisibilityProgressBar.observe(this, progresBarObserver)

//        vModel.synchroHeaderEvent.observer(this, Observer {
//
//            Log.d("logde----->", "synchroHeaderEvent) Completed")
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.hostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
            }
        }

    }

    private fun showProgresBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgresBar(){
        progressBar.visibility = View.GONE
    }



}


