package jds.projects.ddcompanion.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.main.fragments.playersheets.PlayerSheetsFragment
import jds.projects.ddcompanion.activities.settings.SettingsActivity
import jds.projects.ddcompanion.my_classes.adapters.FixedSectionsPagerAdapter
import jds.projects.ddcompanion.my_classes.utils.DayNightActivity


class MainActivity : DayNightActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //inizializzo l'activity
        initActivity(savedInstanceState)

        //IMPOSTO IL VIEWPAGER
        val viewPager = findViewById<ViewPager2>(R.id.activity_main_pager)
        viewPager.adapter =
            FixedSectionsPagerAdapter(
                this,
                viewModel.getFragments()
            )
        //SETTO IL NUMERO DI PAGINE FUORI DALLA VISIONE DELL'UTENTE
        //IN QUESTO MODO GIà INIZIA A CREARLE E LE TIENE SALVATE
        viewPager.offscreenPageLimit = MainActivityViewModel.NUM_FRAGMENTS

        //IMPOSTO IL TAB LAYOUT E LO COLLEGO AL VIEWPAGER
        val tabLayout = findViewById<TabLayout>(R.id.activity_main_tabs)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewModel.getTabTitle(position)
        }.attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updateActualPosition(position)
            }
        })
        setOnLanguageChangedListener(object : OnLanguageChangedListener {
            override fun onLanguageChanged() {
                (viewModel.getFragment(MainActivityViewModel.POSITION_FRAGMENT_PLAYERSHEETS) as PlayerSheetsFragment).notifyLanguageChanged()
            }
        })
    }

    private fun initActivity(savedInstanceState: Bundle?) {
        viewModel =
            MainActivityViewModel(
                applicationContext
            )
        if (savedInstanceState == null || supportFragmentManager.fragments.size == 0) {
            viewModel.createFragments()
        } else {
            supportFragmentManager
            viewModel.loadFragments(supportFragmentManager.fragments.toTypedArray())
        }
        //genero il db base con il manuale base se già non è presente
        viewModel.createStartingRecords(baseContext)
    }

    override fun onBackPressed() {
        val position = viewModel.getActualPosition()
        //se sono nel fragment di player sheets, controllo che non abbia menu aperti
        if (position == MainActivityViewModel.POSITION_FRAGMENT_PLAYERSHEETS) {
            val sheetsFragment = viewModel.getFragment(position) as PlayerSheetsFragment
            //ritorna true se posso chiudere l'app
            if (sheetsFragment.onBackPressed())
                super.onBackPressed()
        } else
            super.onBackPressed()
    }

    override fun onDestroy() {
        //chiudo connessione al db
        viewModel.closeDBConnection()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_preferences -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}